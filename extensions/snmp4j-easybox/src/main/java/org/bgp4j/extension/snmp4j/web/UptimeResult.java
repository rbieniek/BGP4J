/**
 * 
 */
package org.bgp4j.extension.snmp4j.web;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author rainer
 *
 */
@XmlRootElement(name="uptime")
public class UptimeResult {

	private long seconds;
	private long minutes;
	private long hours;
	private long days;
	private long months;
	private long years;
	
	void setStamp(long stamp) {
		stamp /= 1000L; // adjust to seconds;
		
		seconds = stamp % 60;
		stamp /= 60; // adjust to minutes
		
		minutes = stamp % 60;
		stamp /= 60; // adjust to hours
		
		hours = stamp % 24;
		stamp /= 24; // adjust to days
		
		days = stamp % 31;
		stamp /= 31; // adjust to months
		
		months = stamp % 12;
		years = stamp / 12;
	}

	/**
	 * @return the seconds
	 */
	@XmlElement
	public long getSeconds() {
		return seconds;
	}

	/**
	 * @return the minutes
	 */
	@XmlElement
	public long getMinutes() {
		return minutes;
	}

	/**
	 * @return the hours
	 */
	@XmlElement
	public long getHours() {
		return hours;
	}

	/**
	 * @return the days
	 */
	@XmlElement
	public long getDays() {
		return days;
	}

	/**
	 * @return the months
	 */
	@XmlElement
	public long getMonths() {
		return months;
	}

	/**
	 * @return the years
	 */
	@XmlElement
	public long getYears() {
		return years;
	}
}
