package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;


import yahoofinance.Stock;
import yahoofinance.histquotes.Interval;
import yahoofinance.histquotes.HistoricalQuote;
/**This class helps the user managing a Portfolio of Stocks.
 * It accepts as parameters Stocks and it can create a List of Historical Quotes of the Stocks
 * and also an Array of the closing prices of its Stocks.
 * @param Stocks
 *
 * 
 * @author Davide Serpelloni and Alessandro Fina
 *
 */

public class Portfolio {
	
	//Class fields
	public ArrayList<Stock> portfolio= new ArrayList<Stock>();
	public int numberofAssets=portfolio.size();
	public List<List<HistoricalQuote>> History;
	public double[][] log_returnMatrix;
	public double[][] logcorrelationMatrix;
	public ArrayList<Double> quantitiesList=new ArrayList<Double>();
	public double[] volatilities;
	public double[] initialState;
	
	
	//Constructors
	public ArrayList<Stock> addToPortfolio (Stock stock1, double quantities){
		this.portfolio.add(stock1);
		this.numberofAssets=portfolio.size();
		this.quantitiesList.add(quantities);
		return portfolio;
	}
	
	//Class Methods
	public void getInfo(Calendar from, Calendar to) throws IOException{
		List<List<HistoricalQuote>> History = new ArrayList<List<HistoricalQuote>>();
		for(Stock element:this.portfolio){
			History.add(element.getHistory(from, to, Interval.DAILY));
		}
		this.History=History;
		this.log_returnMatrix = getLogYield(History);
		if(numberofAssets!=1) {
			this.logcorrelationMatrix = getLogYieldCorrelationMatrix(log_returnMatrix);
			}
		this.volatilities= volatilities(History);
		this.initialState= getInitialState(History);
		}
	
	//Public Methods
	
	public double[][] getClosingPrices(List<List<HistoricalQuote>> History) {
		double[][] closePriceArray=new double[History.size()][];
		for (int index_of_asset=0;index_of_asset<History.size();index_of_asset++){
			closePriceArray[index_of_asset]=new double[History.get(index_of_asset).size()];;
			for(int index_of_day=0;index_of_day<History.get(index_of_asset).size();index_of_day++){
				closePriceArray[index_of_asset][index_of_day]=History.get(index_of_asset).get(index_of_day).getAdjClose().doubleValue();
						}			
		}
		return closePriceArray;	
		}
	
	//Private Methods
	
	private double[][] getLogYield(List<List<HistoricalQuote>> History) {
		
		double[][] log_returnArray = new double[History.size()][];
		for (int index_of_asset=0;index_of_asset<History.size();index_of_asset++){
			log_returnArray[index_of_asset]= new double[History.get(index_of_asset).size()-1];
			for(int index_of_day =0; index_of_day< History.get(index_of_asset).size()-1; index_of_day++) {
				log_returnArray[index_of_asset][index_of_day]=Math.log(History.get(index_of_asset).get(index_of_day+1).getAdjClose().doubleValue())-Math.log(History.get(index_of_asset).get(index_of_day).getAdjClose().doubleValue());
						}
		}
		return log_returnArray;
		}
		
	private double[] volatilities(List<List<HistoricalQuote>> History) {
		
		double[] volat = new double[History.size()];
		Covariance d = new Covariance();
		for( int i=0; i < History.size(); i++) {
			
			volat[i] = Math.sqrt(d.covariance(getLogYield(History)[i], getLogYield(History)[i]));
		}
		return volat;
		}
	
	private double[][] getLogYieldCorrelationMatrix(double[][] log_returnMatrix)
	{
		Array2DRowRealMatrix log_returnRealMatrix=new Array2DRowRealMatrix(log_returnMatrix);
		RealMatrix log_returnRealMatrixT= log_returnRealMatrix.transpose();
		PearsonsCorrelation log_returnCorrelationMatrix= new PearsonsCorrelation(log_returnRealMatrixT);
		
		return log_returnCorrelationMatrix.getCorrelationMatrix().getData();
	}
	
	private double[] getInitialState(List<List<HistoricalQuote>> History) {
		
	double[] InitialState = new double[History.size()];
	for(int i =0; i< History.size(); i++) {
		InitialState[i] = History.get(i).get(History.get(i).size()-1).getAdjClose().doubleValue()*quantitiesList.get(i);
	}
	return InitialState;}

}
		
		
		
		

	
	
