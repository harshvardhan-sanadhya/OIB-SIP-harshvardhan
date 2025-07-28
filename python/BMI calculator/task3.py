weight=float(input("Enter your weight (in kg): "))
height=float(input("Enter your height (in meters): "))

bmi=weight/(height**2)

if bmi <18.5:
    catagory="Underweight"
elif 18.5 <= bmi <24.9:
    catagory="Normal weight"
elif 25<=bmi <29.9:
    catagory="Overweight"
else:
    catagory="Obese"

print(f"Your BMI is {bmi:.2f}({catagory})")