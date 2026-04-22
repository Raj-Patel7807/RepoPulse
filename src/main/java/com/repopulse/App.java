package com.repopulse;

import com.repopulse.auth.cli.AuthCLI;
import com.repopulse.infra.exception.AppException;
import com.repopulse.infra.exception.ErrorPrinter;
import com.repopulse.infra.session.Session;
import com.repopulse.user.cli.DashboardCLI;

public class App {
    private final AuthCLI authCLI = new AuthCLI();
    private final DashboardCLI dashboardCLI = new DashboardCLI();

    public void start() {
        while(true) {
            try {
                if(Session.getCurrentUser() == null) {
                    authCLI.start();
                } else {
                    dashboardCLI.start();
                }
            } catch(AppException e) {
                ErrorPrinter.print(e);
            } catch(Exception e) {
                ErrorPrinter.printUnexpected(e);
            }
        }
    }
}
