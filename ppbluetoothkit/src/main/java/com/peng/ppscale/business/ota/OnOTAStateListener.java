package com.peng.ppscale.business.ota;

public class OnOTAStateListener {

    /**
     * @param state 0普通的失败 1设备已在升级中不能再次启动升级 2设备低电量无法启动升级 3未配网 4 充电中
     */
    public void onUpdateFail(int state) {
    }

    public void onStartUpdate() {
    }

    public void onUpdateProgress(int progress) {
    }

    public void onUpdateSucess() {
    }

    public void onReadyToUpdate() {
    }

    public void onUpdateEnd() {
    }

}
