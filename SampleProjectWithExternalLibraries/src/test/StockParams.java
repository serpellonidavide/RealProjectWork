package test;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import net.finmath.montecarlo.RandomVariable;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

/**
 * Questa classe permette di ricavare informazioni con frequenza giornaliera da un'azione, dato un intervallo di tempo.
 * Il fine di questa classe è di creare istanze di oggetti contenenti informazioni utili per calcolare prezzi di strumenti
 * derivati.
 * 
 * @author alefi
 *
 */
public class StockParams extends Stock {
	
	public StockParams(String symbol, Calendar from, Calendar to) throws IOException {
		super(symbol);
		this.symbol=symbol;
		getInfo(from,to);
		
	}
	public List<HistoricalQuote> History;
	public double InitialValue;
	public double volatility;
	public String symbol;
	public double drift;
	
	
	
	private void getInfo(Calendar from, Calendar to) throws IOException {
		List<HistoricalQuote> History = getHistory(from, to,Interval.DAILY);
		this.History=History;
		this.volatility=volatility();
		this.InitialValue= YahooFinance.get(symbol).getQuote().getPreviousClose().doubleValue();

		
	}
	private double volatility(){
		
		
		double[] vector = new double[History.size()];
		for(int i=0; i < History.size()-1; i++) {
			
			vector[i] = Math.log(History.get(i+1).getAdjClose().doubleValue())-Math.log(History.get(i).getAdjClose().doubleValue());
		}
		RandomVariable logReturns = new RandomVariable(0,vector);
		return logReturns.getStandardDeviation()*Math.sqrt(252);
		
	}
	
	
	
	
	
	
	
}
