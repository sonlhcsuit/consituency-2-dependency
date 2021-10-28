/*
 * Created on 2007 aug 24
 */
package se.vxu.msi.malteval.grouping;

import se.vxu.msi.malteval.corpus.MaltSentence;
import se.vxu.msi.malteval.metric.Metric;
import se.vxu.msi.malteval.util.DataContainer;

/**
 * All grouping strategies must implement this interface.
 * 
 * @author Jens Nilsson (jens.nilsson@msi.vxu.se)
 */
public interface Grouping extends Metric {
	/**
	 * MaltEval creates once instance of each class that implements Grouping. This method is called once right after creation, and can contain anything that is useful for the other methods when they are called during the evaluation. Creating the Vectors that are returned by
	 * <code>getValidAttributes()</code> and <code>getDefaultAttributes()</code> could, among other things be done here.
	 */
	public void initialize();

	/**
	 * @param sentence
	 *            The sentence that is processed for the moment
	 * @param wordIndex
	 *            The index of the word processed for the moment. The virtual root the the sentence has index 0.
	 * @return The key that represents the sentence and word index.
	 */
	public Object getKey(MaltSentence sentence, int wordIndex);

	/**
	 * Indicates whether this grouping strategy can be part of a complex grouping strategy. If you don't know what a complex grouping strategy is, then put the line <code>return false;</code> as the only statement in this method.
	 * 
	 * @return <code>true</code> or <code>false</code> depending on whether this grouping strategy could be part of a complex grouping or not.
	 */
	public boolean isComplexGroupItem();

	/**
	 * This method is called when the evaluation for this grouping is completed for each parsed file. In the usual case, the input argument is simply returned, but in more complicated grouping strategies (i.e. the the grouping ErrorPropagation), the evaluation data is further processed.
	 * 
	 * @param dataContainer
	 *            The evaluation data created using a grouping strategy
	 * @return A DataContainer containing the post processed data
	 */
	public DataContainer postProcess(DataContainer dataContainer);
	
	/**
	 * This method is called when the table containing the data is created. If this methods return true, the column headers and aggregation data (Row mean and Row count) is displayed.
	 * 
	 * @return If this methods return true, the column headers and aggregation data (Row mean and Row count) is displayed.
	 */
	public boolean showTableHeader();

	/**
	 * This method is called when the table containing the data is created. If this methods return true, the column headers and aggregation data (Row mean and Row count) is displayed. This value can be overridden by the flag --details
	 * 
	 * @return If this methods return true, the detailed table info is shown.
	 */
	public boolean showDetails();
	
	/**
	 * In case the metric flag is given the value "self", then the returned string of this method is shown in the header of the evaluation output for Metric. 
	 * Use <code>return getClass().getSimpleName();</code> as the only statement of this methods if you don't have good reasons not to.
	 * 
	 * @return The self metric name. Usually the the name of the class itself should be used.
	 */
	public String getSelfMetricName();
	

	/**
	 * This method has a special meaning for some of the grouping strategies that are comes with MaltEval for backward compatibility. All new grouping strategies can safely type <code>return null;</code> here.
	 * @return <code>null</code>
	 */
	public String correspondingMetric();
	
	
	/**
	 * MaltEval can not for the moment determine whether a grouping strategy is only able to produce accuracy, 
	 * or if the result must be divided into for instance precision and recall. This method should contain either the line
	 * <code>return true;</code> or <code>return false;</code>.
	 * 
	 * @return true or false, where true means that is does not make sense to divide the evaluation into precision and recall.
	 */
	public boolean isSimpleAccuracyGrouping();
}
