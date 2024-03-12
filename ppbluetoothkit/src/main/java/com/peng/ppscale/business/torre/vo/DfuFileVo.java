package com.peng.ppscale.business.torre.vo;

import java.io.Serializable;

public class DfuFileVo implements Serializable {


    private Long buildTime;
    private String deviceSource;
    private String packageVersion;
    private PackagesBean packages;

    public Long getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Long buildTime) {
        this.buildTime = buildTime;
    }

    public String getDeviceSource() {
        return deviceSource;
    }

    public void setDeviceSource(String deviceSource) {
        this.deviceSource = deviceSource;
    }

    public String getPackageVersion() {
        return packageVersion;
    }

    public void setPackageVersion(String packageVersion) {
        this.packageVersion = packageVersion;
    }

    public PackagesBean getPackages() {
        return packages;
    }

    public void setPackages(PackagesBean packages) {
        this.packages = packages;
    }

    public static class PackagesBean {
        private McuBean mcu;
        private BleBean ble;
        private ResBean res;

        public McuBean getMcu() {
            return mcu;
        }

        public void setMcu(McuBean mcu) {
            this.mcu = mcu;
        }

        public BleBean getBle() {
            return ble;
        }

        public void setBle(BleBean ble) {
            this.ble = ble;
        }

        public ResBean getRes() {
            return res;
        }

        public void setRes(ResBean res) {
            this.res = res;
        }

        @Override
        public String toString() {
            return "mcu:" + mcu + "\n" +
                    "ble:" + ble + "\n" +
                    "res:" + res;
        }

        public static class McuBean {
            private String filename;
            private String version;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            @Override
            public String toString() {
                return filename + "\n" +
                        "version:" + version;
            }
        }

        public static class BleBean {
            private String filename;
            private String version;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            @Override
            public String toString() {
                return filename + "\n" +
                        "version:'" + version;
            }
        }

        public static class ResBean {
            private String filename;
            private String version;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }

            @Override
            public String toString() {
                return filename + "\n" +
                        "version:'" + version;
            }
        }
    }

    @Override
    public String toString() {
        return "\n" +
                "buildTime:" + buildTime + "\n" +
                "deviceSource:" + deviceSource + "\n" +
                "packageVersion:" + packageVersion + "\n" +
                packages;
    }
}
