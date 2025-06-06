import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class User {
    String userName, password;
    User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    void registerUser(Scanner sc) {
        System.out.println("Enter your username : ");
        this.userName = sc.nextLine();

        System.out.println("Enter your password : ");
        this.password = sc.nextLine();

        if (this.userName == null || this.userName.isEmpty() || this.password == null || this.password.isEmpty()) {
            System.out.println("invalid input");
            System.exit(0);
        }

    }

    boolean loginUser(String userName, String password) {
        return this.userName.equals(userName) && this.password.equals(password);
    }
}

class Consumer extends User {
    Consumer(String userName, String password) {
        super(userName, password);   
    }
}

class PremiumUser extends Consumer {
    PremiumUser(String userName, String password) {
        super(userName, password);
    }

    @Override 
    void registerUser(Scanner sc) {
        super.registerUser(sc);
        System.out.println("registered successfully");
    }
    
    void registerPremiumUser(List<PremiumUser> premiumUsers, Scanner sc) {
        PremiumUser newPremiumUser = new PremiumUser(null, null);
        newPremiumUser.registerUser(sc);
        premiumUsers.add(newPremiumUser);
    }

    @Override
    boolean loginUser(String userName, String password) {
        boolean s = super.loginUser(userName, password);
        return s;
    }
    
    boolean loginUser(List<PremiumUser> premiumUsers, String userName, String password) {
        for (PremiumUser premU : premiumUsers) {
            if (premU.userName.equals(userName.trim()) && premU.password.equals(password.trim())) {
                System.out.println("Login successfull");
                return true;
            } 
        }
        System.out.println("Invalid credentials");
        return false;
    }
}

class FreemiumUser extends Consumer {
    FreemiumUser(String userName, String password) {
        super(userName, password);
    }

    @Override 
    void registerUser(Scanner sc) {
        super.registerUser(sc);
        System.out.println("registered successfully");
    }

    void registerFreemiumUser(List<FreemiumUser> freemiumUsers, Scanner sc) {
        FreemiumUser newFreemiumUser = new FreemiumUser(null, null);
        newFreemiumUser.registerUser(sc);
        freemiumUsers.add(newFreemiumUser);
    }

    @Override
    boolean loginUser(String userName, String password) {
        boolean s =super.loginUser(userName, password);
        return s;
    }

    boolean loginUser(List<FreemiumUser> freemiumUsers, String userName, String password)  {
        for (FreemiumUser freeU : freemiumUsers) {
            if (freeU.userName.equals(userName.trim()) && freeU.password.equals(password.trim())) {
                System.out.println("Login successfull");
                return true;
            }
        }
        System.out.println("Invalid credentials");
        return false;
    }
}

class Client extends User {
    String currenLocation;
    Vehicle vehicle;

    Client(String userName, String password) {
        super(userName, password);
    }

    @Override
    void registerUser(Scanner sc) {
        super.registerUser(sc);
        
    }

    void registerClient(List<Client> clients, Scanner sc,List<Client> bikeClients,List<Client> autoClients, List<Client> carClients, List<Client> busClients) {
        Client newClient = new Client(null, null);
        newClient.registerUser(sc);
        System.out.println("Enter the location you're available at : ");
        newClient.currenLocation = sc.nextLine();
        String places[] = {"MG Road", "HSR Layout" , "Church Street", "Amrita Nagar", "Kasanavanahalli"}; boolean isValidPlace = false;
        for (int i=0;i<places.length;i++) {if(places[i].equalsIgnoreCase(newClient.currenLocation)){isValidPlace=true;break;}} 
        if (!isValidPlace) {System.out.println("invalid place"); System.exit(0);}
        System.out.println("Enter vehicle you are going to use for the service (Bike, Auto, Car, Bus) : ");
        String newClientVehicle = sc.nextLine().trim();
        Vehicle chosen;
        switch (newClientVehicle.toLowerCase()) {
            case "bike":
                chosen = new Bike();
                break;
            case "auto":
                chosen = new Auto();
                break;
            case "car":
                chosen = new Car();
                break;
            case "bus":
                chosen = new Bus();
                break;
            default:
                System.out.println("Enter valid input");
                System.exit(0);
                return;
            }
        newClient.assignVehicle(chosen);
        clients.add(newClient);
        newClient.addToList(bikeClients,autoClients,carClients,busClients);
    }

    void addToList(List<Client> bikeClients,List<Client> autoClients, List<Client> carClients, List<Client> busClients) {
        switch (this.vehicle) {
            case Bike bike:
                bikeClients.add(this);
                break;
            case Auto auto:
                autoClients.add(this);
                break;
            case Car car:
                carClients.add(this);
                break;
            case Bus bus:
                busClients.add(this);
                break;
            default:
                break;                
        } 
    }

    boolean loginClient(List<Client> clients, String userName, String password) {
        for (Client cl : clients) {
            if (cl.userName.equalsIgnoreCase(userName) && cl.password.equalsIgnoreCase(password)) {
                System.out.println("Login successful");
                return true;
            }
            
        }
        System.out.println("invalid credentials");
        return false;
    }
    
    void assignVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

}

abstract class Vehicle {
    String vehicleType;
    String[] places = {"MG Road", "HSR Layout" , "Church Street", "Amrita Nagar", "Kasanavanahalli"};
    Vehicle(String vT) {
        this.vehicleType = vT;
    }

    abstract double calcPrice(int duration, boolean is_prem, int day, int hour,List<Client> cls);

    static class BookingTime { int day,hour; BookingTime(int d,int h){day=d;hour=h;} }
    
}

class Bike extends Vehicle {

    List<BookingTime> bookings = new ArrayList<>();
    Bike() {
        super("Bike");
    }

    @Override
    double calcPrice(int duration, boolean is_prem, int day, int hour, List<Client> cls) {
        int count = 0;
        for (var bt : bookings) {
            if (bt.day == day && bt.hour == hour) {count++;}
        }
        bookings.add(new BookingTime(day,hour));
        
    
        if (count==0) {
            return duration * 2 * (is_prem? 0.8 : 1.0);
        } 
        
        if (!cls.isEmpty()) { cls.remove(cls.size()-1);}

        if (cls.isEmpty()) {
            System.out.println("No Bikes left to depart. Please try again later.");
            System.exit(0);
        }

        if (cls.size() <=5) { return duration * 2 * (is_prem? 0.8 : 1.0) * 3;}


        return duration * 2 * (is_prem? 0.8 : 1.0);
    }
    
}

class Auto extends Vehicle {
    List<BookingTime> bookings = new ArrayList<>();
    Auto() {
        super("Auto");
    }


    @Override
    double calcPrice(int duration, boolean is_prem, int day, int hour, List<Client> cls) {
        int count = 0;
        for (var bt : bookings) {
            if (bt.day == day && bt.hour == hour) {count++;}
        }
        bookings.add(new BookingTime(day,hour));
        
    
        if (count==0) {
            return duration * 3 * (is_prem? 0.8 : 1.0);
        } 
        
        if (!cls.isEmpty()) { cls.remove(cls.size()-1);}

        if (cls.isEmpty()) {
            System.out.println("No Autos left to depart. Please try again later.");
            System.exit(0);
        }

        if (cls.size() <=5) { return duration * 3 * (is_prem? 0.8 : 1.0) * 3;}


        return duration * 3 * (is_prem? 0.8 : 1.0);
    }
}

class Car extends Vehicle {
    List<BookingTime> bookings = new ArrayList<>();
    Car() {
        super("Car");
    }


    @Override
    double calcPrice(int duration, boolean is_prem, int day, int hour, List<Client> cls) {
        int count = 0;
        for (var bt : bookings) {
            if (bt.day == day && bt.hour == hour) {count++;}
        }
        bookings.add(new BookingTime(day,hour));
        
    
        if (count==0) {
            return duration * 5 * (is_prem? 0.8 : 1.0);
        } 
        
        if (!cls.isEmpty()) { cls.remove(cls.size()-1);}

        if (cls.isEmpty()) {
            System.out.println("No Cars left to depart. Please try again later.");
            System.exit(0);
        }

        if (cls.size() <=5) { return duration * 5 * (is_prem? 0.8 : 1.0) * 3;}


        return duration * 5 * (is_prem? 0.8 : 1.0);
    }

}

class Bus extends Vehicle {
    List<BookingTime> bookings = new ArrayList<>();
    Bus() {
        super("Bus");
    }


    @Override
    double calcPrice(int duration, boolean is_prem, int day, int hour, List<Client> cls) {
        int count = 0;
        for (var bt : bookings) {
            if (bt.day == day && bt.hour == hour) {count++;}
        }
        bookings.add(new BookingTime(day,hour));
        
    
        if (count==0) {
            return duration * 1 * (is_prem? 0.8 : 1.0);
        } 
        
        if (!cls.isEmpty()) { cls.remove(cls.size()-1);}

        if (cls.isEmpty()) {
            System.out.println("No Buses left to depart. Please try again later.");
            System.exit(0);
        }

        if (cls.size() <=5) { return duration * 1 * (is_prem? 0.8 : 1.0) * 3;}


        return duration * 1 * (is_prem? 0.8 : 1.0);
    }
}

public class TSmain {
    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        PremiumUser initialPremiumUser = new PremiumUser("premiumUser", "premiumpassword");
        FreemiumUser initialFreemiumUser = new FreemiumUser("freemiumUser", "freemiumpassword");
        Client initialClient1 = new Client("client1","1pass");
        Vehicle c1Vehicle = new Bike();
        initialClient1.assignVehicle(c1Vehicle);
        initialClient1.currenLocation = "HSR Layout";

        Client initialClient2 = new Client("client2","2pass");
        Vehicle c2Vehicle = new Auto();
        initialClient2.assignVehicle(c2Vehicle);
        initialClient2.currenLocation = "MG Road";

        Client initialClient3 = new Client("client3","3pass");
        Vehicle c3Vehicle = new Car();
        initialClient3.assignVehicle(c3Vehicle);
        initialClient3.currenLocation = "Church Street";

        Client initialClient4 = new Client("client4","4pass");
        Vehicle c4Vehicle = new Bus();
        initialClient4.assignVehicle(c4Vehicle);
        initialClient4.currenLocation = "Amrita Nagar";

        List<PremiumUser> premiumUsers = new ArrayList<>();
        premiumUsers.add(initialPremiumUser);

        List<FreemiumUser> freemiumUsers = new ArrayList<>();
        freemiumUsers.add(initialFreemiumUser);
        
        List<Client> clients = new ArrayList<>();
        clients.add(initialClient1);
        clients.add(initialClient2);
        clients.add(initialClient3);
        clients.add(initialClient4);

        List<Client> bikeClients = new ArrayList<>();
        bikeClients.add(initialClient1);

        List<Client> autoClients = new ArrayList<>();
        autoClients.add(initialClient2);

        List<Client> carClients = new ArrayList<>();
        carClients.add(initialClient3);

        List<Client> busClients = new ArrayList<>();
        busClients.add(initialClient4);

        
        while (true) {
            System.out.println("Welcome to TS");
            System.out.println("Are you : ");
            System.out.println("The always 1 User or The double 2 Client (or just want to exit by 3 ?) ");
            int UOC = sc.nextInt();
            sc.nextLine();
            TSFlow0 initflow0 = new TSFlow0();
            initflow0.startFlow(UOC, premiumUsers, freemiumUsers,clients,bikeClients,autoClients,carClients,busClients,sc);

        }
    }
}


class TSFlow0 {
    void startFlow(int n, List<PremiumUser> premiumUsers, List<FreemiumUser> freemiumUsers,List<Client> clients,List<Client> bikeClients,List<Client> autoClients,List<Client> carClients,List<Client> busClients, Scanner sc) {
        switch (n) {
            case 1:
                TSFlow1Consumer initflow1 = new TSFlow1Consumer();
                boolean[] loginResult = initflow1.TSflow1Consumer(premiumUsers,freemiumUsers, sc);
                if (!loginResult[0]) { System.out.println("Exiting."); System.exit(0);}
                boolean is_prem = loginResult[1];
                Duration duration = new Duration();
                int[] results = duration.calculateDuration(sc);
                int durationOfTravel = results[0];
                int day = results[1];
                int hour = results[2];
                System.out.println("Your journey will be " + durationOfTravel + " minutes long.");
                System.out.println("Enter the vehicle on which you want to take a ride on ! : ");
                System.out.println("1 => Bike");
                System.out.println("2 => Auto");
                System.out.println("3 => Car");
                System.out.println("4 => Bus");
                int rideCode = sc.nextInt();
                TSFlow1Alpha initflow1Alpha = new TSFlow1Alpha();
                double cost = initflow1Alpha.TSflow1Alpha(rideCode, is_prem, durationOfTravel, day, hour,bikeClients,autoClients,carClients,busClients);
                System.out.println("The total cost of the journey will be : " + cost);
                break;
            case 2:
                TSFlow2Client initflow2 = new TSFlow2Client();
                initflow2.TSflow2(clients, sc,bikeClients,autoClients,carClients,busClients);
                break;
            case 3:
                System.out.println("Thank you. Visit again");
                System.exit(0);    
            default:
                System.out.println("Invalid input. Exiting");
                System.exit(0);
                break;
        }
    }
}

class TSFlow1Consumer {
     boolean[] TSflow1Consumer(List<PremiumUser> premiumUsers, List<FreemiumUser> freemiumUsers, Scanner scanner) {
        boolean is_prem = false;
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");

        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 

        switch (choice) {
            case 1:
                System.out.println("1. Login as Premium User");
                System.out.println("2. Login as Freemium User");

                System.out.print("Enter your choice: ");
                int loginChoice = scanner.nextInt();
                scanner.nextLine(); 

                switch (loginChoice) {
                    case 1:
                        PremiumUser premiumUser = new PremiumUser("", "");
                        System.out.println("Enter Username : ");
                        String us = scanner.nextLine();
                        System.out.println("Enter password : ");
                        String pw = scanner.nextLine();
                        boolean preuserExists = premiumUser.loginUser(premiumUsers, us,pw);
                        if (preuserExists) {
                            is_prem = true;
                            return new boolean[] {true, is_prem};
                        } 
                        return new boolean[] {false, is_prem};
                        
            
                    case 2:
                        FreemiumUser freemiumUser = new FreemiumUser("", "");
                        System.out.println("Enter Username : ");
                        String usf = scanner.nextLine();
                        System.out.println("Enter password : ");
                        String pwf = scanner.nextLine();
                        boolean freeuserExists = freemiumUser.loginUser(freemiumUsers, usf, pwf);
                        if (freeuserExists) {
                            return new boolean[] {freeuserExists, false};
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                return new boolean[] {false, false};
            case 2:
                System.out.println("1. Register as Premium User");
                System.out.println("2. Register as Freemium User");

                System.out.print("Enter your choice: ");
                int registerChoice = scanner.nextInt();
                scanner.nextLine(); 

                switch (registerChoice) {
                    case 1:
                        PremiumUser newPremiumUser = new PremiumUser("", "");
                        newPremiumUser.registerPremiumUser(premiumUsers, scanner);
                        
                        is_prem = true;
                        return new boolean[] {true, is_prem};
                    case 2:
                        FreemiumUser newFreemiumUser = new FreemiumUser("", "");
                        newFreemiumUser.registerFreemiumUser(freemiumUsers, scanner);
                        return new boolean[] { true, is_prem};
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
                return new boolean[] {false, false};
            case 3:
                System.out.println("Exiting...");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return new boolean[] {false, false};
    }

}

class CalPlaceDuration {
    String places[] = {"MG Road", "HSR Layout" , "Church Street", "Amrita Nagar", "Kasanavanahalli"};
    int[] placeDuration(String firstCity,String secondCity) {
        boolean citiesPresent = false;
        if (firstCity == secondCity || firstCity.isEmpty() || secondCity.isEmpty()) {
            System.out.println("Enter valid input");
            System.exit(0);
        }
        int minuteDistance = 0;
        
        for (int i = 0; i<places.length;i++) {
            if (places[i].equalsIgnoreCase(firstCity)) {
                for (int j =i+1;j<places.length;j++) {
                    if (places[j].equalsIgnoreCase(secondCity)) {
                        citiesPresent = true;
                        minuteDistance = indexdiff(i, j);
                        
                    }
                }
                for (int j=0;j<i;j++) {
                    if (places[j].equalsIgnoreCase(secondCity)) {
                        citiesPresent = true;
                        minuteDistance = indexdiff(j, i);
                        
                    }
                }
            }
            
        }
        if (citiesPresent) {
            return new int[] {minuteDistance};
        } else {
            throw new RuntimeException("Invalid city.");
        }
    }
    int indexdiff(int i, int j) {
        int a = j-i;
        switch (a) {
            case 1:
                return 5;
            case 2:
                return 10;
            case 3:
                return 15;
            case 4:
                return 20;
            default:
                return 0;
        }
    }
}

class Duration {
    void checkDay(int d) {
        if (d<1 || d>7) {
            System.out.println("Invalid day");
            System.exit(0);
        }
    }
    int[] calculateDuration(Scanner sc) {
        System.out.println("Enter your current location : ");
        String location = sc.nextLine();
        System.out.println("Enter your drop-off location : ");
        String dropOff = sc.nextLine();
        CalPlaceDuration placeDists = new CalPlaceDuration();
        int[] results = placeDists.placeDuration(location, dropOff);
        int minuteDist = results[0];
        
        System.out.println("Enter the Day (1 - Sunday.....7 - Saturday): ");
        int day = sc.nextInt();
        checkDay(day);
        System.out.println("Enter the hour of the day (0 - 23): ");
        int hour = sc.nextInt();
        checkTime(hour);
        if (hour >16 && hour < 21 && (day == 1 || day == 7 || day == 6 || day == 2)) {
            return new int[]  {minuteDist + 20 + 10, day, hour};
        } else if (hour >16 && hour < 21) {
            return new int[]  {minuteDist + 20, day, hour};
        } else if (day == 1 || day == 7 || day == 6 || day == 2) {
            return new int[] {minuteDist + 10, day, hour};
        } else {
            return new int[] {minuteDist, day, hour};
        }
    } 
    void checkTime(int hour) {
        if (hour > 23 || hour < 0) {
            System.out.println("Invalid hour");
            System.exit(0);
        }
    }
}

class TSFlow1Alpha {
    double TSflow1Alpha(int rideCode, boolean is_prem, int durationOfTravel, int day, int hour,List<Client> bikeClients,List<Client> autoClients,List<Client> carClients,List<Client> busClients) {
        if (rideCode != 1 && rideCode != 2 && rideCode !=3 && rideCode!= 4) {
            System.out.println("Invalid input");
            System.exit(0);
        }
        switch (rideCode) {
            case 1:
                Vehicle rideB = new Bike();
                return rideB.calcPrice(durationOfTravel, is_prem,day,hour,bikeClients);
            case 2:
                Vehicle rideA = new Auto();
                return rideA.calcPrice(durationOfTravel, is_prem,day,hour,autoClients);
            case 3:
                Vehicle rideC = new Car();
                return rideC.calcPrice(durationOfTravel, is_prem,day, hour, carClients);
            case 4:
                Vehicle rideBus = new Bus();
                return rideBus.calcPrice(durationOfTravel, is_prem,day,hour, busClients);
            default:
                System.out.println("Enter valid input ");
                break;        
        }
        return Double.NaN;
    }
}

class TSFlow2Client {
    void TSflow2(List<Client> clients, Scanner sc,List<Client> bikeClients,List<Client> autoClients,List<Client> carClients,List<Client> busClients) {
        System.out.println("Welcome Client");
        System.out.println("Choose from the options below : ");
        System.out.println("1) Login");
        System.out.println("2) Register");
        int choice = sc.nextInt();
        sc.nextLine();
        switch (choice)  {
            case 1:
                System.out.println("Enter Username : ");
                String us = sc.nextLine();
                System.out.println("Enter password : ");
                String pw = sc.nextLine();
                Client clientLog = new Client(us, pw);
                boolean logCheck = clientLog.loginClient(clients, us, pw);
                if (logCheck) { System.out.println("Welcome back !");}        
                break;
            case 2:
                
                Client regClient = new Client("", "");
                regClient.registerClient(clients, sc,bikeClients,autoClients,carClients,busClients);
                System.out.println("Welcome Aboard !");
                break;
            
            default :
                System.out.println("Enter a valid input");
                System.exit(0);        
                break;
        }
    }
}