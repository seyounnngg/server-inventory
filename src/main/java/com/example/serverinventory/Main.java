package com.example.serverinventory;

import com.example.serverinventory.repository.*;
import com.example.serverinventory.service.ServerCheckService;
import com.example.serverinventory.service.ServerService;
import com.example.serverinventory.service.UserService;
import com.example.serverinventory.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserDbRepository();
        ServerRepository serverRepository = new ServerDbRepository();
        ServerCheckRepository serverCheckRepository = new ServerCheckDbRepository();

        UserService userService = new UserService(userRepository);
        ServerService serverService = new ServerService(serverRepository);
        ServerCheckService serverCheckService = new ServerCheckService(serverCheckRepository);

        ConsoleView view = new ConsoleView(userService, serverService, serverCheckService);
        view.start();
    }
}
