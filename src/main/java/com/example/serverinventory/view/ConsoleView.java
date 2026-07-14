package com.example.serverinventory.view;

import com.example.serverinventory.service.ServerCheckService;
import com.example.serverinventory.service.ServerService;
import com.example.serverinventory.service.UserService;

import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final ServerService serverService;
    private final ServerCheckService serverCheckService;

    public ConsoleView(UserService userService, ServerService serverService, ServerCheckService serverCheckService) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverCheckService = serverCheckService;
    }

    public void start() {
        while (true) {
            printMenu();
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 0) {
                break;
            } else if (choice == 1) {
                registerUser();
            }
        }
    }

    private void registerUser() {
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("팀: ");
        String team = scanner.nextLine();

        userService.registerUser(name, team);
        System.out.println("등록 완료!");
    }

    private void printMenu() {
        System.out.println("1. 팀원 등록");
        System.out.println("2. 서버 등록");
        System.out.println("0. 종료");
        System.out.print("선택: ");
    }
}
