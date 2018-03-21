#select max(DATESERVER) as maxdateserver,min(DATESERVER) as mindDateserver from price_xauusd60  ;

#select DATESERVER,TIMESERVER,DATELOCAL,TIMELOCAL from price_xauusd60 where TIMESERVER=TIMELOCAL  ;


#select min(publishdate),max(publishdate) from economic_data;
#select min(a.OCCURDATE),max(OCCURDATE) from economic_news a;

#select DISTINCT PUBLISHDATE from economic_data;
select DISTINCT OCCURDATE from economic_event order by occurdate;
select  DISTINCT OCCURDATE from economic_holiday order by occurdate;


SELECT
	a.*, date_format(a.ModifyDate, '%H%m%s') modifyTime,
	b.indicator AS indicatorName,
	b.indicatoreffect,
	b.matchrate,
	b.analysisreport
FROM
	economic_data a LEFT JOIN
	economic_indicator b
on
	a.indicatorId = b.id
where publishdate >= '20161110'
AND publishdate <= '20161110' 
ORDER BY
	publishdate DESC,
	publishTime ASC,
	a.country,
	case 
		when  a.importance='高' then	0 
		when  a.importance='中' then	1 
		when  a.importance='低' then	2
		 ELSE 100
	 end,
	a.indicatorId,
	b.indicator