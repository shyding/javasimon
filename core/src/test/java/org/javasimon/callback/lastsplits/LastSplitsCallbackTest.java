package org.javasimon.callback.lastsplits;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.javasimon.EnabledManager;
import org.javasimon.Manager;
import org.javasimon.SimonUnitTest;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author gquintana
 */
public class LastSplitsCallbackTest extends SimonUnitTest {

	private Manager manager = new EnabledManager();

	@BeforeClass
	public void addCallback() {
		manager.callback().addCallback(new LastSplitsCallback(5));
	}

	@BeforeMethod
	public void resetStopwatch() {
		getStopwatch().reset();
	}

	@Test
	public void testAddSplit() {
		addSplit(100L);
		addSplit(150L);
		addSplit(125L);
		addSplit(150L);
		LastSplits lastSplits = getLastSplits();
		assertEquals(100L, lastSplits.getMin().longValue());
		assertEquals(150L, lastSplits.getMax().longValue());
		assertEquals((150 * 2 + 125 + 100) / 4L, lastSplits.getMean().longValue());
	}

	@Test
	public void testTrend() {
		addSplit(100L);
		addSplit(125L);
		addSplit(150L);
		LastSplits lastSplits = getLastSplits();
		assertTrue(lastSplits.getTrend() > 0, "Positive trend");
		resetStopwatch();
		addSplit(150L);
		addSplit(125L);
		addSplit(100L);
		lastSplits = getLastSplits();
		assertTrue(lastSplits.getTrend() < 0, "Negative trend");
	}

	private Stopwatch getStopwatch() {
		return manager.getStopwatch(getClass().getName() + ".stopwatch");
	}

	private LastSplits getLastSplits() {
		return (LastSplits) getStopwatch().getAttribute(LastSplitsCallback.ATTR_NAME_LAST_SPLITS);
	}

	private void addSplit(long length) {
		getStopwatch().addSplit(Split.create(length, manager));
	}
}
