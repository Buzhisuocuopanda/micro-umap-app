package com.mkst.umap.base.core.util;

import com.mkst.mini.systemplus.common.config.Global;
import gnu.io.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

/**
 * <pre>
 *  串口管理
 *  Created by gin on 2019-07-29.
 * </pre>
 */
@Slf4j
public class SerialPortManager {
	
	private static final int baudrate = 9600;
	
	private static SerialPortManager instance;
	
	/** 扫码枪串口 **/
	private SerialPort scanSerialPort;
	
	public SerialPortManager() {
		try {
			// TODO 这里改成数据库配置
			scanSerialPort = openPort(Global.getConfig("serial.port.scan"), baudrate);
			scanSerialPortListener();
			if(scanSerialPort != null) {
				log.info("扫描枪串口打开成功");
			}
		} catch (Exception e) {
			log.error("扫描枪串口打开失败：", e);
		}
		
	}
	
	public static SerialPortManager getInstance() {
		if(instance == null) {
			instance = new SerialPortManager();
		}
		return instance;
	}
	
	public SerialPort getScanSerialPort() {
		return scanSerialPort;
	}
	
	/**
	 * 扫描枪端口添加监听
	 */
	private void scanSerialPortListener() {
		// 添加串口监听
		SerialPortManager.addListener(scanSerialPort, new SerialPortManager.DataAvailableListener() {

			@Override
			public void dataAvailable() {
				try {
					if (scanSerialPort == null) {
						log.error("扫码枪串口对象为空，监听失败！");
					} else {
						// 读取串口数据
						byte[] data = readFromPort(scanSerialPort);
						String readData = new String(data).trim();
						log.info("监听到扫描枪串口数据：" + readData);
					}
				} catch (Exception e) {
					log.error("解析二维码数据错误：{}", e.getMessage(), e);
				}
			}
		});
	}

	/**
	 * 打开串口
	 * 
	 * @param portName
	 *            端口名称
	 * @param baudrate
	 *            波特率
	 * @return 串口对象
	 * @throws PortInUseException
	 *             串口已被占用
	 * @throws UnsupportedCommOperationException 
	 * @throws NoSuchPortException 
	 * @throws  
	 */
	public final SerialPort openPort(String portName, int baudrate) throws PortInUseException, UnsupportedCommOperationException, NoSuchPortException {
		// 通过端口名识别端口
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		// 打开端口，并给端口名字和一个timeout（打开操作的超时时间）
		CommPort commPort = portIdentifier.open(portName, 2000);
		// 判断是不是串口
		if (commPort instanceof SerialPort) {
			SerialPort serialPort = (SerialPort) commPort;
			// 设置一下串口的波特率等参数
			// 数据位：8
			// 停止位：1
			// 校验位：None
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
			return serialPort;
		}
		return null;
	}

	/**
	 * 关闭串口
	 * 
	 * @param serialport
	 *            待关闭的串口对象
	 */
	public void closePort(SerialPort serialPort) {
		if (serialPort != null) {
			serialPort.close();
		}
	}

	/**
	 * 从串口读取数据
	 * 
	 * @param serialPort
	 *            当前已建立连接的SerialPort对象
	 * @return 读取到的数据
	 */
	public byte[] readFromPort(SerialPort serialPort) {
		InputStream in = null;
		byte[] bytes = {};
		try {
			in = serialPort.getInputStream();
			// 缓冲区大小为一个字节
			byte[] readBuffer = new byte[1];
			int bytesNum = in.read(readBuffer);
			while (bytesNum > 0) {
				bytes = ArrayUtils.concat(bytes, readBuffer);
				bytesNum = in.read(readBuffer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}

	/**
	 * 添加监听器
	 * 
	 * @param port
	 *            串口对象
	 * @param listener
	 *            串口存在有效数据监听
	 */
	private static void addListener(SerialPort serialPort, DataAvailableListener listener) {
		try {
			// 给串口添加监听器
			serialPort.addEventListener(new SerialPortListener(listener));
			// 设置当有数据到达时唤醒监听接收线程
			serialPort.notifyOnDataAvailable(true);
			// 设置当通信中断时唤醒中断线程
			serialPort.notifyOnBreakInterrupt(true);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 串口监听
	 */
	private static class SerialPortListener implements SerialPortEventListener {

		private DataAvailableListener mDataAvailableListener;

		public SerialPortListener(DataAvailableListener mDataAvailableListener) {
			this.mDataAvailableListener = mDataAvailableListener;
		}

		@Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
			switch (serialPortEvent.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE: // 1.串口存在有效数据
				if (mDataAvailableListener != null) {
					mDataAvailableListener.dataAvailable();
				}
				break;

			case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2.输出缓冲区已清空
				break;

			case SerialPortEvent.CTS: // 3.清除待发送数据
				break;

			case SerialPortEvent.DSR: // 4.待发送数据准备好了
				break;

			case SerialPortEvent.RI: // 5.振铃指示
				break;

			case SerialPortEvent.CD: // 6.载波检测
				break;

			case SerialPortEvent.OE: // 7.溢位（溢出）错误
				break;

			case SerialPortEvent.PE: // 8.奇偶校验错误
				break;

			case SerialPortEvent.FE: // 9.帧错误
				break;

			case SerialPortEvent.BI: // 10.通讯中断
				log.error("与串口设备通讯中断");
				break;

			default:
				break;
			}
		}
	}

	/**
	 * 串口存在有效数据监听
	 */
	public interface DataAvailableListener {
		/**
		 * 串口存在有效数据
		 */
		void dataAvailable();
	}
	
}