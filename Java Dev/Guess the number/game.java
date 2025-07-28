import java.util.Random;
import java.util.Scanner;
class game{
static int ran_easy(){
    Random rand = new Random();
    int target=rand.nextInt(50)+1;
    return target;
}
static int ran_hard(){
    Random rand = new Random();
    int target=rand.nextInt(1000)+1;
    return target;
}
    public static void main(String[] args) {
        int target=0;
        String play;
        do{
        System.out.println("Welcome to Guessing the Number Game");
        System.out.println("please select a mode");
        System.out.println("1.Easy \n2.Hard");
       Scanner sc = new Scanner(System.in);
       String choice=sc.nextLine();

       if(choice.equalsIgnoreCase("EASY")){
        target=ran_easy();
        System.out.println("Welcome in the Easy Mode");
        System.out.println("Here you get 10 chances and you need to guess the number between 1 to 50");
        int count=0;
        System.out.println("Enter your number");
        for(int i=0;i<10;i++){
            int guess=sc.nextInt();
           
            if (guess == target){
                System.out.println("You Predicted the right answer :"+target);
                break;
            }
            else if(target>guess){
                System.out.println("Guess Higher");
            }
            else{
                System.out.println("Guess Lower");
            }
            count++;
            if(count==10){
            System.out.println("You Lost");
            break;
        }
        }
        

       }
       else if(choice.equalsIgnoreCase("Hard")){
        target=ran_hard();
        System.out.println("Welcome in the Hard Mode");
        System.out.println("Here you get 10 chances and you need to guess the number between 1 to 1000");
        int count=1;
        System.out.println("Enter your number");
        for(int i=0;i<10;i++){
            int guess=sc.nextInt();
           
            if (guess == target){
                System.out.println("You Predicted the right answer :"+target);
                break;
            }
            else if(target>guess){
                System.out.println("Guess Higher");
            }
            else{
                System.out.println("Guess Lower");
            }
            count++;
        }
        if(count==11){
            System.out.println("You Lost");
        }
        else{
            int score=(100-(count*10))+10;
            System.out.println("Total Attempt:"+count);
            System.out.println("Your Score is :"+score);
        }
       }
       else{
        System.out.println("Please select a correct mode");
       }
       System.out.print("Do You wan to play again (yes/no)\n");
       sc.nextLine();
       play=sc.nextLine().toLowerCase();
    }while(play.equalsIgnoreCase("yes"));

    System.out.println("Thankyou for playing");
}
}