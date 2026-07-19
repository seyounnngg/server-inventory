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

    private User loggedInUser;

    public ConsoleView(UserService userService, ServerService serverService, ServerCheckService serverCheckService) {
        this.userService = userService;
        this.serverService = serverService;
        this.serverCheckService = serverCheckService;
    }

    public void start() {
        while (true) {
            System.out.println();
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 종료");
            System.out.print("선택: ");
            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("프로그램을 종료합니다.");
                break;
            } else if (choice.equals("1")) {
                if (login()) {
                    boolean continueProgram = runMainMenu();
                    if (!continueProgram) {
                        break;
                    }
                }
            } else if (choice.equals("2")) {
                signUp();
            } else {
                System.out.println("잘못된 선택입니다.");
            }
        }
    }

    private boolean runMainMenu() {
        while (true) {
            printMenu();
            try {
                int choice = Integer.parseInt(scanner.nextLine());

                if (choice == 0) {
                    return false;
                } else if (choice == 1) {
                    updateUser();
                } else if (choice == 2) {
                    boolean stillLoggedIn = deleteUser();
                    if (!stillLoggedIn) {
                        return true;
                    }
                } else if (choice == 3) {
                    registerServer();
                } else if (choice == 4) {
                    listServers();
                } else if (choice == 5) {
                    viewServersByOwner();
                } else if (choice == 6) {
                    updateServer();
                } else if (choice == 7) {
                    deleteServer();
                } else if (choice == 8) {
                    recordServerCheck();
                } else if (choice == 9) {
                    checkAllServers();
                } else if (choice == 10) {
                    viewCheckHistory();
                } else if (choice == 11) {
                    viewDownServers();
                } else {
                    System.out.println("존재하지 않는 메뉴입니다.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력해주세요.");
            }
        }
    }

    private boolean login() {
        System.out.println();
        System.out.print("아이디: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        User user = userService.login(username, password);
        if (user == null) {
            System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
            return false;
        }

        loggedInUser = user;
        System.out.println(user.getName() + "님, 환영합니다!");
        return true;
    }

    private void signUp() {
        System.out.println();
        System.out.print("이름: ");
        String name = scanner.nextLine();
        System.out.print("팀: ");
        String team = scanner.nextLine();
        System.out.print("아이디: ");
        String username = scanner.nextLine();
        System.out.print("비밀번호: ");
        String password = scanner.nextLine();

        userService.registerUser(name, team, username, password);
        System.out.println("회원가입 완료! 다시 로그인해주세요.");
    }

    private void updateUser() {
        System.out.print("새 이름: ");
        String name = scanner.nextLine();

        System.out.print("새 팀: ");
        String team = scanner.nextLine();

        userService.updateUser(loggedInUser.getId(), name, team);
        loggedInUser.setName(name);
        loggedInUser.setTeam(team);
        System.out.println("내 정보 수정 완료!");
    }

    private boolean deleteUser() {
        System.out.print("정말 계정을 삭제하시겠습니까? (Y/N): ");
        String confirm = scanner.nextLine();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("취소되었습니다.");
            return true;
        }

        userService.deleteUser(loggedInUser.getId());
        System.out.println("계정이 삭제되었습니다.");
        loggedInUser = null;
        return false;
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
        System.out.println();
        System.out.println("내 담당 서버: " + loggedInUser.getName());
        List<Server> servers = serverService.getServerByOwnerId(loggedInUser.getId());

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

    private void viewDownServers() {
        List<Server> servers = serverService.getAllServers();

        List<Server> downServers = servers.stream()
                .filter(server -> {
                    ServerCheck latest = serverCheckService.getLatestCheck(server.getId());
                    return latest != null && latest.getStatus().equals("DOWN");
                })
                .toList();

        if (downServers.isEmpty()) {
            System.out.println("현재 DOWN 상태인 서버가 없습니다.");
            return;
        }

        System.out.println();
        System.out.println("=== 현재 DOWN 상태인 서버 ===");
        for (Server server : downServers) {
            long daysSince = serverCheckService.getDaysSinceLastCheck(server.getId());
            System.out.println(server.getId() + " - " + server.getHostname()
                    + " (" + server.getIpAddress() + ") - 마지막 점검: " + daysSince + "일 전");
        }
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
        System.out.println("--- 계정 관리 ---");
        System.out.println("1. 내 정보 수정");
        System.out.println("2. 회원 탈퇴");
        System.out.println("--- 서버 관리 ---");
        System.out.println("3. 서버 등록");
        System.out.println("4. 서버 목록 조회");
        System.out.println("5. 내 담당 서버 조회");
        System.out.println("6. 서버 수정");
        System.out.println("7. 서버 삭제");
        System.out.println("--- 점검 관리 ---");
        System.out.println("8. 서버 점검하기");
        System.out.println("9. 전체 서버 일괄 점검");
        System.out.println("10. 서버 점검 이력 보기");
        System.out.println("11. DOWN 상태 서버 조회");
        System.out.println("0. 종료");
        System.out.println();
        System.out.print("선택: ");
    }
}