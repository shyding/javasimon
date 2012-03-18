package org.javasimon.callback.lastsplits;

import org.javasimon.Simon;
import org.javasimon.Split;
import org.javasimon.Stopwatch;
import org.javasimon.callback.CallbackSkeleton;
import org.javasimon.callback.logging.LogTemplate;
import static org.javasimon.callback.logging.LogTemplates.*;

/**
 * Callback retaining the last N stopwatch splits.
 *
 * @author gquintana
 * @since 3.2.0
 */
public class LastSplitsCallback extends CallbackSkeleton {
	/**
	 * Simon attribute name of the LastSplit object stored
	 * in Simons
	 */
	public static final String ATTR_NAME_LAST_SPLITS = "lastSplits";

	/**
	 * Number of splits retained in each Simon.
	 * Default 10
	 */
	private final int capacity;

	/**
	 * Global flag indicating whether last splits should be logged once in
	 * a while
	 */
	private boolean logEnabled=false;
	/**
	 * SLF4J log template shared by all stopwatches
	 */
	private final LogTemplate<Split> enabledStopwatchLogTemplate=toSLF4J(getClass().getName(),"debug");
	/**
	 * Default constructor with a buffer capacity of 10
	 */
	public LastSplitsCallback() {
		this.capacity = 10;
	}

	/**
	 * Constructor
	 *
	 * @param capacity Buffer capacity
	 */
	public LastSplitsCallback(int capacity) {
		this.capacity = capacity;
	}

	/**
	 * Get the LastSplits object from Simon attributes
	 *
	 * @param stopwatch Stopwatch
	 * @return LastSplits object
	 */
	private LastSplits getLastSplits(Stopwatch stopwatch) {
		return (LastSplits) stopwatch.getAttribute(ATTR_NAME_LAST_SPLITS);
	}
	
	/**
	 * When Stopwatch is created, a Last Splits attributes is added
	 */
	@Override
	public void onSimonCreated(Simon simon) {
		if (simon instanceof Stopwatch) {
			Stopwatch stopwatch = (Stopwatch) simon;
			LastSplits lastSplits=new LastSplits(capacity);
			lastSplits.setLogTemplate(createLogTemplate(stopwatch));
			stopwatch.setAttribute(ATTR_NAME_LAST_SPLITS, lastSplits);
		}

	}

	/**
	 * When a Splits is stopped, it's added to the stopwatch a Last Splits attribute
	 */
	@Override
	public void onStopwatchStop(Split split) {
		LastSplits lastSplits = getLastSplits(split.getStopwatch());
		lastSplits.add(split);
		lastSplits.log(split);
	}

	/**
	 * When the Stopwatch is reseted, the Last splits attribute as well
	 */
	@Override
	public void onSimonReset(Simon simon) {
		if (simon instanceof Stopwatch) {
			getLastSplits((Stopwatch) simon).clear();
		}
	}

	public boolean isLogEnabled() {
		return logEnabled;
	}
	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}
	/**
	 * Create log template for given stopwatch.
	 * This method can be overriden to tune logging strategy.
	 * By default, when {@link #logEnabled] is set, last splits are logged at each buffer revolution.
	 * @param stopwatch Stopwatch
	 * @return Log template
	 */
	protected LogTemplate<Split> createLogTemplate(Stopwatch stopwatch) {
		LogTemplate<Split> logTemplate;
		if (logEnabled) {
			logTemplate=everyNSplits(enabledStopwatchLogTemplate, capacity);
		} else {
			logTemplate=disabled();
		}
		return logTemplate;
	}
}