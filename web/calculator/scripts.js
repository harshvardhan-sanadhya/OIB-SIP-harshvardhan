let input1=document.getElementById("yes1");
let input2=document.getElementById("yes2");
let lastans="";
document.querySelectorAll("button").forEach(button =>{
    button.addEventListener("click",()=>{
        const val=button.textContent;
        if (val ==="Enter"){
            try{
                const expression= input2.innerText
            .replace(/x|X/g,'*')
            .replace(/÷/g,'/')
            .replace(/√(\d+(\.\d+)?)/g,'Math.sqrt($1)')
            .replace(/\^/g,'**')
                const result = eval(expression);
                lastans=result;
                input1.innerText=input2.innerText;
                input2.innerText=result;
            }catch(e){
                input2.innerText="Error"
            }
        }
        else if (val==="clear"){
            input1.innerText="";
            input2.innerText="";
        }
        else if (val === "del"){
            input2.innerText = input2.innerText.slice(0,-1);
        }
        else if (val==="ans"){
            input2.innerText+=lastans;
        }
        else{
            input2.innerText += val;
        }
    })
});