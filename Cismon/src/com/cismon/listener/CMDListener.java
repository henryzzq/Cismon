package com.cismon.listener;

public interface CMDListener {
	public boolean init();
	public String getMessage();
	public String messageFilter(String msg);
	public boolean destroy();
}
