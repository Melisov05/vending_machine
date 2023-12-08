import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;
import java.time.LocalDate;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    LocalDate currentDate = LocalDate.now();
    int currentYear = currentDate.getYear();


    static Scanner sc = new Scanner(System.in);

    private PayingMethods payingMethods;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        getPayMethod();
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Available amount of money: " + payingMethods.getAmount());

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        if(payingMethods.getMethod().equalsIgnoreCase("Credit card")){
            chooseActionCard(allowProducts);
        } else if(payingMethods.getMethod().equalsIgnoreCase("coin")){
            chooseAction(allowProducts);
        }

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (payingMethods.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        print(" a - Пополнить баланс монетами");
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            payingMethods.setAmount(payingMethods.getAmount() + 10);
            print("Вы пополнили баланс на 10");
            return;
        }
        if ("h".equalsIgnoreCase(action)) {
            isExit = true;
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    payingMethods.setAmount(payingMethods.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попрбуйте еще раз.");
            chooseAction(products);
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }

    private void getPayMethod(){
        try{
            System.out.println("Choose a payment method.");
            System.out.print("Press 'a' for coins, press 'b' for credit/debit card: ");
            String str = sc.nextLine();
            switch (str){
                case "a":
                    payingMethods = new CoinAcceptor(100, "coin");
                    break;
                case "b":
                    if(cardAuthentification()){
                        payingMethods = new CreditCard(500, "credit card");
                    }
                    break;
                default:
                    System.out.println("You picked a wrong letter, try again");
                    getPayMethod();
            }
        } catch (IllegalArgumentException e){
            print("You wrote something wrong, try again");
            getPayMethod();
        }
    }

    private void chooseActionCard(UniversalArray<Product> products) {
        showActions(products);
        print(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("h".equalsIgnoreCase(action)) {
            isExit = true;
            return;
        }
        try {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    payingMethods.setAmount(payingMethods.getAmount() - products.get(i).getPrice());
                    print("Вы купили " + products.get(i).getName());
                    break;
                } else if ("h".equalsIgnoreCase(action)) {
                    isExit = true;
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            print("Недопустимая буква. Попробуйте еще раз.");
            chooseActionCard(products);
        }
    }
    public static boolean checkLuhn(String cardNo)
    {
        cardNo = cardNo.replaceAll("\\s+", "");
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond == true)
                d = d * 2;

            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }

    private boolean cardAuthentification(){
        System.out.print("Enter your credit/debit card number: ");
        String cardNumber = sc.nextLine();
        if(checkLuhn(cardNumber)){
            try {
                System.out.print("Enter card's month of expiration: ");
                int expirationMonth = Integer.parseInt(sc.nextLine());
                if(expirationMonth > 12 || expirationMonth < 1){
                    System.out.println("You entered incorrect expiration month. Try again.");
                    cardAuthentification();
                }
                System.out.print("Enter card's year of expiration: ");
                int expirationYear = Integer.parseInt(sc.nextLine());
                if((expirationYear > (currentYear + 5)) || (currentYear > expirationYear)){
                    System.out.println("You entered incorrect expiration year. Try again.");
                    cardAuthentification();
                }
                System.out.print("Enter card's cvv: ");
                int cvv = Integer.parseInt(sc.nextLine());
                if((cvv > 999) || (cvv < 1)){
                    System.out.println("You entered incorrect cvv. Try again.");
                    cardAuthentification();
                }
                System.out.println("Success! Your card is accepted");
                return true;
            } catch (NumberFormatException e){
                System.out.println("Either expiration date or cvv is incorrect");
                cardAuthentification();
            }
        } else{
            System.out.println("The card number is not valid, try again");
            cardAuthentification();
        }
        return false;
    }
}
