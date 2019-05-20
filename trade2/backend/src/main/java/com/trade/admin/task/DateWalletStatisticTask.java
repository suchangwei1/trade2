package com.trade.admin.task;

import com.trade.service.admin.DateWalletStatisticService;
import org.springframework.beans.factory.annotation.Autowired;

public class DateWalletStatisticTask implements TaskExecutor {
    @Autowired
    private DateWalletStatisticService dateWalletStatisticService;

    @Override
    public void execute() {
        dateWalletStatisticService.nonBuildWalletStatisticLog();
    }
}
