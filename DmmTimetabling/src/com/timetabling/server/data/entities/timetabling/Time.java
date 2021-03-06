package com.timetabling.server.data.entities.timetabling;

import java.util.Random;


/** Describes specific lesson time.
 *  For example: 1st lesson in Monday on lower week */
public class Time {
	
	public enum WeekType{
		
		/**Lower week (������ ������)*/ LOWER (0),
		
		/**Upper week (������� ������)*/UPPER (1),
		
		/**Not flashing week (������� ���������� ����)*/ FULL (2),
		
		UNDEF (3), 
		
		/** ANY recommended to use for Wish.*/ ANY (4);
		
		int code;
		private WeekType(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
		static public WeekType getByCode(int code) {
			switch (code) {
			case 0: return LOWER;
			case 1: return UPPER;
			case 2: return FULL;
			case 3: return UNDEF;
			case 4: return ANY;
			default: return UNDEF;
			}
		}
	}
	
	/** ANY recommended to use for Wish  */
	public final static int ANY = -1;
	public final static int UNDEF = -2;
	public final static int NUMBER_OF_DAYS = 5;
	private final static int NUMBER_OF_LESSONS = 6;
	
	/** Monday, Tuesday, ... */
	private int day = UNDEF;
	private int lessonNumber = UNDEF;
	private int weekTypeCode = WeekType.UNDEF.getCode();
	
	public Time() {}
	
	public Time(int timeKey) {
		lessonNumber = timeKey / 10000;
		day = (timeKey - lessonNumber*10000) / 100;
		weekTypeCode = timeKey - lessonNumber*10000 - day*100;
	}

	public int getWeekDay() {
		return day;
	}

	public void setWeekDay(int weekDay) {
		this.day = weekDay;
	}

	public int getLessonNumber() {
		return lessonNumber;
	}

	public void setLessonNumber(int lessonNumber) {
		this.lessonNumber = lessonNumber;
	}

	public int getWeekTypeCode() {
		return weekTypeCode;
	}

	public void setWeekTypeCode(int weekTypeCode) {
		this.weekTypeCode = weekTypeCode;
	}

	public WeekType getWeekType() {
		return WeekType.getByCode(weekTypeCode);
	}

	public void setWeekType(WeekType weekType) {
		this.weekTypeCode = weekType.getCode();
	}
	
	public int getKey() {
		return weekTypeCode + 100*day + 10000*lessonNumber;
	}
	
	// TODO it is just for Real lessons (has day, lesson number and week type)
	public boolean hasConflictWith(Time t) {
		if (day==t.day)
			if (lessonNumber==t.lessonNumber)
				if (weekTypeCode==t.weekTypeCode
				 || weekTypeCode==WeekType.FULL.code
				 || t.weekTypeCode==WeekType.FULL.code)
					return true;
		return false;
	}
	
	
	private static Random rand = new Random();
	
	public static Time getRandomTime(boolean isFlushing) {
		return new Time(
				     (isFlushing? (rand.nextBoolean()?0:1) : 2) +
				     rand.nextInt(NUMBER_OF_DAYS)*100 +
				     rand.nextInt(NUMBER_OF_LESSONS)*10000);
	}
	
	public String toString() {
		String dayStr;
		switch (day) {
		case 0:  dayStr = "пн";  break;
		case 1:  dayStr = "вт";  break;
		case 2:  dayStr = "ср";  break;
		case 3:  dayStr = "чт";  break;
		case 4:  dayStr = "пт";  break;
		case 5:  dayStr = "сб";  break;
		case 6:  dayStr = "вс";  break;
		default: dayStr = "??";  break;
		}
		return dayStr + " " + (lessonNumber+1);
	}

}
