package com.company;

import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        String command;
        Jedis jedis = new Jedis();

        System.out.println(jedis.get("ping"));
        long len = jedis.llen("queue#tasks");
        for(int i = 0; i < len; i++) {
            list.add(jedis.rpop("queue#tasks"));
        }
        printTaskList(list);

        Scanner sc = new Scanner(System.in);

        while (true) {
            printInterface();
            command = sc.nextLine();
            if(command.trim().equalsIgnoreCase("a")) {
                System.out.println("Enter your task on each line. Enter empty line to finish adding task.");
                String task = sc.nextLine();
                while (!task.trim().isEmpty()){
                    list.add(task);
                    task = sc.nextLine();
                }

            } else if(command.trim().equalsIgnoreCase("d")) {
                System.out.print("Enter task number you wish to delete: ");
                String task = sc.nextLine();
                try {
                    int taskNum = Integer.parseInt(task);
                    list.remove(taskNum - 1);
                } catch (NumberFormatException e) {
                    System.out.println("Your input is not a number.");
                }

            } else if (command.trim().equalsIgnoreCase("q")) {
                for (String l : list) {
                    jedis.lpush("queue#tasks", l);
                }
                break;
            } else {
                System.out.println("Unregistered command.");
            }
            printTaskList(list);
        }
    }

    private static void printInterface() {
        System.out.print("Press [a] or [d] to add or delete your task respectively (press [q] to save and quit): ");
    }

    private static void printTaskList(ArrayList<String> list) {
        System.out.println("Your task list");
        for(int i = 0; i < list.size(); i++) {
            System.out.println("[" + (i+1) +  "] " + list.get(i));
        } if(list.size() == 0) {
            System.out.println("[Empty]");
        }
    }
}
