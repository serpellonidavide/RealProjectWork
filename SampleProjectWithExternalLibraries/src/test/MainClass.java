package test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.*;


import net.finmath.exception.CalculationException;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloMultiAssetBlackScholesModel;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationInterface;



import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;



/**
 * A very simple console application showing how you can download data.
 * For more information, please refer to 
 * 
 * https://financequotes-api.com/
 * https://github.com/sstrickx/yahoofinance-api
 * 
 * 
 * @author Alessandro Gnoatto
 *
 */
public class MainClass {

	
	public static void main(String[] args) throws IOException, CalculationException {
		
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		//https://stackoverflow.com/questions/11022934/getting-java-net-protocolexception-server-redirected-too-many-times-error
		
		
		Calendar from = Calendar.getInstance();
		Calendar to = Calendar.getInstance();
		
		from.add(from.YEAR, -5); //5 years in the past
		
		Stock Google = YahooFinance.get("GOOG");
		Stock Tesla = YahooFinance.get("TSLA");
		Stock Facebook = YahooFinance.get("FB");
		Stock Apple = YahooFinance.get("AAPL");
		Stock GeneralMotors = YahooFinance.get("GM");

		//System.out.println(Tesla);
		//System.out.println(Google);
		

		//Portfolio costruction

		
		Portfolio savings =new Portfolio();
		savings.addToPortfolio(Google, 100);
		
		
	
		savings.getInfo(from, to);
		
		System.out.println(LinearizedLoss.Mean(savings, 252));
		System.out.println(LinearizedLoss.Variance(savings, 252));

	
		TimeDiscretizationInterface h = new TimeDiscretization(0,1000,1);
		//double[] r = savings.getInitialState(ValoriStorici);
		//double[] v = savings.volatilities(ValoriStorici);
		//double[][] hshs = savings.getLogYield(ValoriStorici);
		//double[][] corr = savings.getLogYieldCorrelationMatrix(hshs);
		//System.out.println(v[0]);
		//MonteCarloMultiAssetBlackScholesModel provino = new MonteCarloMultiAssetBlackScholesModel(h,10,r,0.0,v,corr);
		
		System.out.println(savings.volatilities);
		//System.out.println(savings.volatility*savings.volatility);
		//System.out.println(savings.getInitialState(ValoriStorici)[0]);
		
		System.out.println();
		
	}	
		
	}

