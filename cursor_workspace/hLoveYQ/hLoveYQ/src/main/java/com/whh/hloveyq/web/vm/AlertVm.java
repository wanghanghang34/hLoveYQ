package com.whh.hloveyq.web.vm;

public class AlertVm {
	private final String code;
	private final String title;
	private final String message;
	private final String severity;

	public AlertVm(String code, String title, String message, String severity) {
		this.code = code;
		this.title = title;
		this.message = message;
		this.severity = severity;
	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getSeverity() {
		return severity;
	}
}

