package test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.*;


import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.products.EuropeanOption;
import net.finmath.modelling.Model;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloMultiAssetBlackScholesModel;
import net.finmath.stochastic.RandomVariableInterface;
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
		
		System.out.println("Il var è " + LinearizedLoss.getVar(savings, 252, 0.95));
		//System.out.println("La varianza è " + LinearizedLoss.Variance(savings, 252));
		//System.out.println("La media è " + LinearizedLoss.Mean(savings, 252));
		//System.out.println(savings.portfoliovalue);
		
		//MonteCarlo Var Test
	
		TimeDiscretizationInterface h = new TimeDiscretization(0,365,1);
		double[] r = savings.initialState;
		double[] v = savings.volatilities;
		double[][] corr = savings.logreturncorrelationMatrix;
	
		AssetModelMonteCarloSimulationInterface provino = new MonteCarloMultiAssetBlackScholesModel(h,20000,r,0.0,v,corr);
		RandomVariableInterface Goog = provino.getAssetValue(252, 0);
		RandomVariableInterface Tesl = provino.getAssetValue(252, 1);
		RandomVariableInterface FB = provino.getAssetValue(252, 2);
		RandomVariableInterface App = provino.getAssetValue(252, 3);
		RandomVariableInterface GM = provino.getAssetValue(252, 4);
		
		
		
		RandomVariableInterface Somma = Goog.add(Tesl).add(FB).add(App).add(GM).sub(savings.portfoliovalue);
		
		
		
		System.out.println(Somma.getVariance());
		System.out.println("Il var secondo MonteCarlo è " + ( -Somma.getQuantile(0.95)));
		
		//Option Valuation
		
		TimeDiscretizationInterface q = new TimeDiscretization(0,365,1);
		MonteCarloBlackScholesModel prova2 = new  MonteCarloBlackScholesModel(q,20000,savings.initialState[0]/savings.quantitiesList.get(0),0,savings.volatilities[0]);
		OptionVar Ex2 = new OptionVar(365,0);
		System.out.println(Ex2.getValue(365,provino).getAverage());
		
		System.out.println(AnalyticFormulas.blackScholesOptionValue(savings.initialState[0],0,savings.volatilities[0],365,savings.initialState[0]/savings.quantitiesList.get(0)));
		
		System.out.println(Ex2.getOptionPayoff(180, prova2, Google.getQuote().getPreviousClose().doubleValue()).getAverage());
		
		
		
		
		
		

	}	
		
	}

