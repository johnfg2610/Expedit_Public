package io.github.johnfg10.handlers;

import io.github.johnfg10.ExpeditConst;

import java.util.TimerTask;

/**
 * Created by johnfg10 on 15/05/2017.
 */
public class onMinuteTimer extends TimerTask {
    @Override
    public void run() {
        ExpeditConst.uptime = ExpeditConst.uptime++;
    }
}
