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

		//Portfolio costruction

		
		Portfolio savings =new Portfolio();
		savings.addToPortfolio(Google, 100);
		savings.addToPortfolio(Tesla, 20);
		savings.addToPortfolio(Facebook, 400);
		savings.addToPortfolio(Apple, 50);
		savings.addToPortfolio(GeneralMotors, 700);
		
		savings.getInfo(from, to);
		
		//Linearized Loss Test
		
		System.out.println("Il var � " + LinearizedLoss.getVar(savings, 1, 0.95));
		System.out.println("La varianza � " + LinearizedLoss.Variance(savings, 252));
		System.out.println("La media � " + LinearizedLoss.Mean(savings, 252));
		System.out.println(savings.portfoliovalue);
		

	
		TimeDiscretizationInterface h = new TimeDiscretization(0,252,1);
		double[] r = savings.initialState;
		double[] v = savings.volatilities;
		double[][] corr = savings.logreturncorrelationMatrix;
	
		MonteCarloMultiAssetBlackScholesModel provino = new MonteCarloMultiAssetBlackScholesModel(h,10000,r,0.0,v,corr);
		System.out.println(provino.toString());
		System.out.println(provino.getAssetValue(252, 0).getAverage());

	}	
		
	}

