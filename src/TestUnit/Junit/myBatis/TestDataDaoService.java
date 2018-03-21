package TestUnit.Junit.myBatis;

import org.junit.Test;

import TestUnit.Proceduretask;

public class TestDataDaoService {

	@Test
	public final void testGetDataDaoString() {
		Proceduretask pt = new Proceduretask();
		pt.setProcedurename("t2");
		// int
		// count=((DataDaoMapper)Session.getDataDao(dbName,DataDaoMapper.class)).getSjshbCount(pt);
		// assertEquals(0, count);

	}

}
