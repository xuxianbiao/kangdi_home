package com.kandi.driver;

import android.os.RemoteException;

import com.driverlayer.kdos_driverServer.IECarDriver;

public class StateDNRInfoDriver {
	
	private IECarDriver R_service;
	
	public StateDNRInfoDriver(IECarDriver R_service) {
		this.R_service = R_service;
		
	}
	
	/**
	 * 读取车辆档位	
	 * @param null																	
	 * @return			返回档位信息 1:D档,2:N档,3:R档；
	 */
	
	public int getCarDNRInfo() throws RemoteException {
		int ret = R_service.getCar_DNR();
		return ret;
	}
	
	/**
	 * 设置汽车档位
	 * @param dnrState	包含三个状态，前进(0x01)，空档(0x02)，倒车(0x03)；
	 * @return			设置成功返回0，失败返回负值错误代码
	 */
	
	public int setCarDNRInfo(int state) throws RemoteException {
		return R_service.setCar_DNR(state);
	}
	
}
