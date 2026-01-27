package com.example.IM.PT.scheduler;

import com.example.IM.PT.Entity.User;
import com.example.IM.PT.service.EmailService;
import com.example.IM.PT.service.ReportService;
import com.example.IM.PT.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@EnableScheduling
public class MailScheduler {

        @Autowired
        private UserService userservice;

        @Autowired
        private ReportService reportService;

        @Autowired
        private EmailService mailService;


        @Scheduled(cron = "0 0 0 * * *")
        public void sendDailyPowerReports() {

            for (User user : userservice.getAllUsers()) {

                if ("ADMIN".equals(user.getDesignation())) {

                    Double power = reportService.getPlantPowerLast24Hours();

                    mailService.sendEmail(
                            user.getEmailId(),
                            "Plant Power Usage - Last 24 Hours",
                            "Total Plant Power Consumption: " + power + " W"
                    );
                }

                if ("DEPTMANAGER".equals(user.getDesignation())) {

                    Double power = reportService.getDepartmentPowerLast24Hours(
                            user.getDepartment()
                    );

                    mailService.sendEmail(
                            user.getEmailId(),
                            "Department Power Usage - Last 24 Hours",
                            "Department: " + user.getDepartment() +
                                    "\nTotal Power Consumption: " + power + " W"
                    );
                }
            }
        }
    }





