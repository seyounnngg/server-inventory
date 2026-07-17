package com.example.serverinventory.view;

import com.example.serverinventory.model.Server;
import com.example.serverinventory.model.ServerCheck;
import com.example.serverinventory.model.User;
import com.example.serverinventory.service.ServerCheckService;
import com.example.serverinventory.service.ServerService;
import com.example.serverinventory.service.UserService;

import java.io.IOException;
import java.net.InetAddress;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);
    private final UserService userService;
    private final ServerService serverService;
    private final ServerCheckService serverCheckService;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ConsoleView(UserService userService, ServerService serverService, ServerCheckService serverCheckService) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverCheckService = serverCheckService;
    }

    public void start() {
        while (true) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 0) {
                    break;
                } else if (choice == 1) {
                    registerUser();
                } else if (choice == 2) {
                    updateUser();
                } else if (choice == 3) {
                    deleteUser();
                } else if (choice == 4) {
                    registerServer();
                } else if (choice == 5) {
                    listServers();
                } else if (choice == 6) {
                    viewServersByOwner();
                } else if (choice == 7) {
                    updateServer();
                } else if (choice == 8) {
                    deleteServer();
                } else if (choice == 9) {
                    recordServerCheck();
                } else if (choice == 10) {
                    checkAllServers();
                } else if (choice == 11) {
                    viewCheckHistory();
                } else {
                    System.out.println("존재하지 않는 메뉴입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private void registerUser() {

        System.out.print("이름: ");
        String name = scanner.nextLine();

        System.out.print("팀: ");
        String team = scanner.nextLine();

        userService.registerUser(name, team);
        System.out.println("팀원 등록 완료!");
    }

    private void updateUser() {

        listUsers();

        System.out.print("수정할 팀원 ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        System.out.print("새 이름: ");
        String name = scanner.nextLine();

        System.out.print("새 팀: ");
        String team = scanner.nextLine();

        userService.updateUser(id, name, team);
        System.out.println("수정 완료!");
    }

    private void deleteUser() {

        listUsers();

        System.out.print("삭제할 팀원 ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        userService.deleteUser(id);
        System.out.println("삭제 완료!");
    }

    private void registerServer() {

        listUsers();

        System.out.print("담당자 id: ");
        Long ownerId = Long.parseLong(scanner.nextLine());

        System.out.print("호스트명: ");
        String hostname = scanner.nextLine();

        System.out.print("IP 주소: ");
        String ipAddress = scanner.nextLine();

        System.out.print("유형 (WEB / DB / APP): ");
        String type = scanner.nextLine();

        serverService.registerServer(hostname, ipAddress, type, ownerId);
        System.out.println("서버 등록 완료!");
    }

    private void listServers() {
        List<Server> servers = serverService.getAllServers();
        System.out.println();
        System.out.println("=== 서버 목록 ===");
        for (Server server : servers) {
            System.out.println(server.getId() + " - " + server.getHostname()
                    + " (" + server.getIpAddress() + ", " + server.getType() + ")");
        }
    }

    private void viewServersByOwner() {

        listUsers();

        System.out.print("담당자 ID: ");
        Long ownerId = Long.parseLong(scanner.nextLine());

        List<Server> servers = serverService.getServerByOwnerId(ownerId);

        if (servers.isEmpty()) {
            System.out.println("담당하는 서버가 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("=== 담당 서버 목록 ===");
        for (Server server : servers) {
            System.out.println(server.getId() + " - " + server.getHostname()
                    + " (" + server.getIpAddress() + ", " + server.getType() + ")");
        }
    }

    private void updateServer() {

        listServers();

        System.out.print("수정할 서버 ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        listUsers();

        System.out.print("새 담당자 ID: ");
        Long ownerId = Long.parseLong(scanner.nextLine());

        System.out.print("새 호스트명: ");
        String hostname = scanner.nextLine();

        System.out.print("새 IP 주소: ");
        String ipAddress = scanner.nextLine();

        System.out.print("새 유형 (WEB / DB / APP): ");
        String type = scanner.nextLine();

        serverService.updateServer(id, hostname, ipAddress, type, ownerId);
        System.out.println("수정 완료!");
    }

    private void deleteServer() {

        listServers();

        System.out.print("삭제할 서버 ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        serverService.deleteServer(id);
        System.out.println("삭제 완료!");
    }

    private void recordServerCheck() {

        listServers();

        System.out.print("점검할 서버 ID: ");
        Long serverId = Long.parseLong(scanner.nextLine());

        Server server = serverService.getServerById(serverId);
        if (server == null) {
            System.out.println("존재하지 않는 서버입니다.");
            return;
        }

        String status;
        try {
            boolean reachable = InetAddress.getByName(server.getIpAddress()).isReachable(2000);
            status = reachable ? "UP" : "DOWN";
        } catch (IOException e) {
            status = "DOWN";
        }

        serverCheckService.recordCheck(serverId, status);
        System.out.println("점검 완료! 상태: " + status);
    }

    private void checkAllServers() {
        List<Server> servers = serverService.getAllServers();
        System.out.println();
        System.out.println("=== 전체 서버 점검 시작 ===");

        for (Server server : servers) {
            String status;
            try {
                boolean reachable = InetAddress.getByName(server.getIpAddress()).isReachable(2000);
                status = reachable ? "UP" : "DOWN";
            } catch (IOException e) {
                status = "DOWN";
            }

            serverCheckService.recordCheck(server.getId(), status);
            System.out.println(server.getHostname() + " - " + status);
        }

        System.out.println("=== 전체 점검 완료 ===");
    }

    private void viewCheckHistory() {

        listServers();

        System.out.print("조회할 서버 ID: ");
        Long serverId = Long.parseLong(scanner.nextLine());

        List<ServerCheck> checkList = serverCheckService.getCheckHistory(serverId);
        if (checkList.isEmpty()) {
            System.out.println("점검 기록이 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("=== 점검 이력 ===");
        for (ServerCheck check : checkList) {
            System.out.println(check.getCheckedAt().format(formatter) + " - " + check.getStatus());
        }

        ServerCheck latest = serverCheckService.getLatestCheck(serverId);
        System.out.println("최신 상태: " + latest.getStatus());

        long daysSince = serverCheckService.getDaysSinceLastCheck(serverId);
        System.out.println("마지막 점검: " + daysSince + "일 전");
    }

    private void listUsers() {
        List<User> users = userService.getAllUsers();
        System.out.println();
        System.out.println("=== 등록된 팀원 목록 ===");
        for (User user : users) {
            System.out.println(user.getId() + " - " + user.getName() + " (" + user.getTeam() + ")");
        }
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- 팀원 관리 ---");
        System.out.println("1. 팀원 등록");
        System.out.println("2. 팀원 수정");
        System.out.println("3. 팀원 삭제");
        System.out.println("--- 서버 관리 ---");
        System.out.println("4. 서버 등록");
        System.out.println("5. 서버 목록 조회");
        System.out.println("6. 담당자별 서버 조회");
        System.out.println("7. 서버 수정");
        System.out.println("8. 서버 삭제");
        System.out.println("--- 점검 관리 ---");
        System.out.println("9. 서버 점검하기");
        System.out.println("10. 전체 서버 일괄 점검");
        System.out.println("11. 서버 점검 이력 보기");
        System.out.println("0. 종료");
        System.out.print("선택: ");
    }
}