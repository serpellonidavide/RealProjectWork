package test;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.*;


import net.finmath.exception.CalculationException;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.montecarlo.assetderivativevaluation.AssetModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloBlackScholesModel;
import net.finmath.montecarlo.assetderivativevaluation.MonteCarloMultiAssetBlackScholesModel;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationInterface;



import yahoofinance.Stock;
import yahoofinance.YahooFinance;


public class MainClass {

	
	public static void main(String[] args) throws IOException, CalculationException {
		
		CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
		
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
		System.out.println("Il valore iniziale del portafoglio è " + savings.portfoliovalue);
		
		//Linearized Loss Test
		
		System.out.println("Il var secondo la linearized loss è " + LinearizedLoss.getVar(savings, 1, 0.95));
		
		//MonteCarlo Var Test
	
		TimeDiscretizationInterface timediscretization = new TimeDiscretization(0,252,1.0/252);
		double[] InitialStates = savings.initialStates;
		double[] Volatilities = savings.volatilities;
		double[][] Correlations = savings.logreturncorrelationMatrix;
	
		AssetModelMonteCarloSimulationInterface PortfolioModel = new MonteCarloMultiAssetBlackScholesModel(
				timediscretization,
				20000,
				InitialStates,
				0.0,
				Volatilities,
				Correlations);
		
		RandomVariableInterface Goog = PortfolioModel.getAssetValue(1.0, 0);
		RandomVariableInterface Telsa = PortfolioModel.getAssetValue(1.0, 1);
		RandomVariableInterface FB = PortfolioModel.getAssetValue(1.0, 2);
		RandomVariableInterface app = PortfolioModel.getAssetValue(1.0, 3);
		RandomVariableInterface Gen = PortfolioModel.getAssetValue(1.0, 4);
		
		RandomVariableInterface Somma = Goog.add(Telsa).add(FB).add(app).add(Gen);
		System.out.println("Il var secondo MonteCarlo è " + (Somma.sub(savings.portfoliovalue)).mult(-1).getQuantile(0.05));
		System.out.println(" ");
		
		
		//Option Valuation
		
		StockParams GoogleParams = new StockParams("GOOG", from, to);
		
		MonteCarloBlackScholesModel prova2 = new  MonteCarloBlackScholesModel	//Creazione di un modello
				(timediscretization,	
				20000,
				GoogleParams.InitialValue,
				0.0,
				GoogleParams.volatility);
		
		
		OptionVar Ex2 = new OptionVar	(GoogleParams,				//Nuova istanza per calcolare misure di perdita
										prova2,
										1,
										GoogleParams.InitialValue);	
		
		System.out.println("Il valore iniziale dell'opzione è " + AnalyticFormulas.blackScholesOptionValue (GoogleParams.InitialValue,
																											0,
																											GoogleParams.volatility,
																											1,
																											Ex2.strike));
		System.out.println("MonteCarlo full valuation var " 	+ Ex2.getVarFullValuation(0.5, prova2));
		System.out.println("MonteCarlo delta var " 				+ Ex2.gerVarDelta(0.5, 0.05));
		System.out.println("MonteCarlo delta-gamma var " 		+ Ex2.gerVarDeltaGamma(0.5, 0.05));
		
	}	
		
	}

