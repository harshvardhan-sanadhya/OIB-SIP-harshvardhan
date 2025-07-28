import requests
import pandas as pd
from newspaper import Article
import time
import random
from datetime import datetime, timedelta
from sumy.parsers.plaintext import PlaintextParser
from sumy.nlp.tokenizers import Tokenizer
from sumy.summarizers.lsa import LsaSummarizer

WAYBACK_API = "http://archive.org/wayback/available"

def get_wayback_url(site, date):
    params = {"url": site, "timestamp": date}
    res = requests.get(WAYBACK_API, params=params).json()
    if "archived_snapshots" in res and res["archived_snapshots"]:
        return res["archived_snapshots"]["closest"]["url"]
    return None

def scrape_article(url):
    try:
        article = Article(url)
        article.download()
        article.parse()
        return article.title, article.text
    except:
        return None, None

def summarize_text(text, sentence_count=3):
    parser = PlaintextParser.from_string(text, Tokenizer("english"))
    summarizer = LsaSummarizer()
    summary = summarizer(parser.document, sentence_count)
    return " ".join(str(sentence) for sentence in summary)

def generate_dates(start, end, step_days=10):
    dates = []
    current = start
    while current <= end:
        dates.append(current.strftime("%Y%m%d"))
        current += timedelta(days=step_days)
    random.shuffle(dates)
    return dates

# -------- CONFIGURATION --------
newspapers = {
    "The Hindu": "https://www.thehindu.com/",
    "Times of India": "https://timesofindia.indiatimes.com/",
    "Indian Express": "https://indianexpress.com/"
}

start_date = datetime(2000, 1, 1)
end_date = datetime(2004, 12, 31)
dates_list = generate_dates(start_date, end_date, step_days=5)

data = []
target_count = 1000

print("✅ Starting news collection...")

for paper, url in newspapers.items():
    for d in dates_list:
        if len(data) >= target_count:
            break
        wayback_url = get_wayback_url(url, d)
        if wayback_url:
            title, text = scrape_article(wayback_url)
            if title and text and len(text) > 300:
                summary = summarize_text(text)
                data.append([paper, title, summary, text])
                print(f"✅ [{len(data)}/{target_count}] {paper}: {title[:60]}")
        time.sleep(2)

# Save to CSV
df = pd.DataFrame(data, columns=["Newspaper", "Title", "Summary", "Description"])
df.to_csv("news_2000_2004_1000.csv", index=False)
print("✅ DONE! Saved 1000 news with summaries to news_2000_2004_1000.csv")