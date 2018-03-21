package TestUnit;


import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.calendar.DailyCalendar;

import com.scher.ScherExecJob;



public class ScheduleRunner {
 
	public void taskCron(String kpiCde) {

		String cronExpress = 0 + "/" + 5+ " * * * * ?";
		try{
			String jobName =kpiCde;
			String jobGroup =kpiCde;
			String triggerName  =kpiCde;
			String triggerGroup = kpiCde;

			SchedulerFactory schedulerFactory = new StdSchedulerFactory();
			Scheduler scheduler = schedulerFactory.getScheduler();

			JobDetail jobDetail = null;
			CronTrigger cronTrigger = null;
		
			try {
				jobDetail = newJob(ScherExecJob.class).withIdentity(jobName,
						jobGroup).build();
				// �����Ȳ���JobActionʹ�ã�
			} catch (Exception e) {
			}
 
			TriggerBuilder<Trigger> triggerBuilder = newTrigger();
			triggerBuilder.withIdentity(triggerName, triggerGroup);
		
			String total_time ="14:23,18:00";
//			String timeZone = total_time.replaceAll(";", ",");
//			timeZone = timeZone.replaceAll("/[", "");
//			timeZone = timeZone.replaceAll("/]", "");
			String[] tz = total_time.split(",");
			for(int i =0;i<tz.length;i++)System.out.println(tz[i]);
				if (tz.length == 2) {
					scheduler.addCalendar(jobName,
							getOneTimeZoneCal(tz), true, true);
				} else if (tz.length == 4) {
					scheduler.addCalendar(jobName,
							getTwoTimeZoneCal(tz), true, true);
				}

				cronTrigger = triggerBuilder
				.modifiedByCalendar(triggerName)
				.withSchedule(
						cronSchedule(cronExpress)
								.withMisfireHandlingInstructionDoNothing())
				.build();
				
				scheduler.scheduleJob(jobDetail, cronTrigger);

				scheduler.start();
				Trigger tr = scheduler.getTrigger(new TriggerKey(jobName,jobName));
				System.out.println(tr.getNextFireTime());
		
			} catch (Exception e) {
				e.printStackTrace();
			}
	 
	}

	private DailyCalendar getOneTimeZoneCal(String[] args) {
		DailyCalendar cal = new DailyCalendar(args[0], args[1]);
		cal.setInvertTimeRange(true);
		return cal;
	}

	private DailyCalendar getTwoTimeZoneCal(String[] args) {
		DailyCalendar cal1 = new DailyCalendar(args[0], args[3]);
		cal1.setInvertTimeRange(true);
		DailyCalendar cal2 = new DailyCalendar(cal1, args[1], args[2]);
		return cal2;
	}
	
	
	public static void main(String[] args) {
		new ScheduleRunner().taskCron("1");
	}  
}
