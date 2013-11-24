/****************************************************************************
* Copyright (C) 2013 Demming (http://github.com/demming)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
***************************************************************************/

import java.util.Scanner;

/**
 * This class enables one to approximate the exponential function exp(x) for all x>=0.
 * And it includes a public static void main method.
 */
//public
class Exponential {
	// definition of colors with \u001b for the unicode escape character \e or \033 -- to be used in console input and output
	private static String 	off		=	"\u001b[0;0m",	// #(\w)\(([^)]+)\);#\1\t=\t\2,\n#g
							red		=	"\u001b[1;31m",
							green	=	"\u001b[1;32m",
							yellow	=	"\u001b[1;33m",
							blue	=	"\u001b[1;34m",
							magenta	=	"\u001b[1;35m",
							cyan	=	"\u001b[1;36m",
							white	=	"\u001b[1;37m",
							black	=	"\u001b[1;30m",
							darkwhite	=	"\u001b[0;37m",
							darkyellow	=	"\u001b[0;33m",
							darkgreen	=	"\u001b[0;32m",
							darkblue	=	"\u001b[0;34m",
							darkcyan	=	"\u001b[0;36m",
							darkred		=	"\u001b[0;31m",
							darkmagenta	=	"\u001b[0;35m",
							darkblack	=	"\u001b[0;30m";

	public static void main(String[] args)
	{
		// instantiate Scanner in scan to parse user input
		Scanner scan = new Scanner(System.in);

		System.out.println("We are almost set to compute the exponential sum for a given base and a given power.");
		System.out.println("Please provide the "+yellow+"base"+off+" for the calculation (e.g., 121.12, 1541, etc.): "+green);
		double base = scan.nextDouble();					///< base of the exponentiation

		System.out.println(off+"Now we're only missing the "+yellow+"power "+off+"(an integer): "+green);
		int n = scan.nextInt();								///< power, i.e., the exponent

		// make sure that neither base nor n is less than 0 as we compute here for positive integers or zero only
		if(base < 0) {
			System.out.println(red+"We haven't expected a negative base. Terminating."+off);
			return;											///< exit program
		}
		if(n < 0) {
			System.out.println(red+"We haven't expected a negative power. Terminating."+off);
			return;
		}

		System.out.println(off+ "We're all set.");			// NOTE how we turn off the color at this position; NOTE: off + off is interpreted by terminals as off

		// tell the user the value of the exponential sum. Note: expSum embedded in the argument list
		System.out.println(cyan+ "The exponential sum is: " +green+ expSum(base, n) +off);

		// inquire of the threshold epsilon that we use for the approximation
		System.out.println("Please provide the approximation threshold "+yellow+"epsilon: "+green);	// eps
		double eps = scan.nextDouble();

		// epsilon must be greater than zero, even 0.000001 or 10^-20
		if(eps <= 0) {
			System.out.println(red+"We haven't expected an epsilon less than or equal to 0. Terminating."+off);
			return;
		}

		// expApproximate is embedded here
		System.out.println(cyan+"The approximation of exp("+base+") is: " +green+ expApproximate_straightforward(base, eps) +off);

		System.out.println(cyan+"The approximation of exp("+base+") is: " +green+ expApproximate_efficient(base, eps) +off);


		// we define further down two different methods for the approximation for up to four valid decimals,
		//	- one of which approximation threshold,
		// 	- the other, however, compares against a fixex number (therefore the second parameter)
		System.out.println(cyan+"Task (g): The least n for the approximation up to four valid decimals for x = 1      is: "+green+ leastTop(1     ,  2.7182) +off);
		System.out.println(cyan+"Task (g): The least n for the approximation up to four valid decimals for x = 3.1416 is: "+green+ leastTop(3.1416, 23.1408) +off);
	}




	/** @brief Teilaufgabe b
	 * 	@param base	the base of the exponentiation
	 * 	@param exp	the exponent of that very exponentiation
	 * 	@return 	the exponentiated base (x^n -- x to the n-th)
	 */
	public static double power(double base, int exp)
	{
		double product = 1;
		for(int i = 0; i < exp; i++)
			product *= base;

		return product;
	}

	/** @brief: Teilaufgabe c
	 * 	@param n	the natural number to thake the factorial of, its base as in n!
	 * 	@return 	the factorial
	 */
	public static double factorial(int n)
	{
		int k;								///< k is the incremented index that goes up to and including n. Declared here.
		double f = k = 1;					///< f is the accumulator of the series, the factorial which we @return. First k assigned 1, then f assigned k's value.

		while(k <= n)						// we reverse the typical product series
			f *= k++;		/// FIXME if f is of type long, it will quickly overflow
// 		System.out.println(n + "! = " + f);
		return f;
	}

	/** @brief Teilaufgabe d
	 * 	@param x	the fixed base which we iteratively exponentiate and divide by the exponent's factorial for the purpose of approximation (converging series)
	 * 	@param n	the number of iteration steps to make, i.e., the number of summands, as we're adding up the elements of the sequence (series)
	 * 	@return 	the exponential sum as computed in this method
	 */
	public static double expSum(double x, int n)
	{
		double sum = 0;

		for(int i = 0; i <= n; i++) {
			sum += power(x, i) / factorial(i);	/// FIXME int overflow, supra
// 			System.out.println("\tx == "+ x +", i == "+ i +" -> "+ power(x,i) +" / "+ factorial(i));
		}
		return sum;
	}

	/** @brief Teilaufgabe f
	 * 	@param x	the argument to the exponential function which we are approximating here, pretend we pass it to the actual exp(x)
	 * 	@param eps	this is the convergence threshold, i.e., if the absolute difference between any two consecutive elements of the sequence we are iterating over narrows down to this eps or turns less than that, then the approximation is perfect relative to this parameter.
	 * 	@return 	the sequence element at which the approximation is perfect adhering to eps
	 */
	public static double expApproximate_efficient(double x, double eps)
	{
		int k = 0;								///< starting index, we start with 1 since there is no x_{-1} prior to x_0, we thus do an initial branching in the do-while loop below

		while(power(x, k) / factorial(k++) > eps);

 		System.out.println(red+"\tx_k == "+white+ expSum(x, k-1) +red+", x_prev == "+white+ expSum(x, k-2) +off);

 		System.out.println(blue+"Approximation complete. k == "+magenta+ (k-1) +off);

		return expSum(x, k-1);
	}


	/**
	 * 	DIFF BETWEEN EVERY TWO SUBSEQUENT X_K IS A STABLE STRUCTURE, I.E., THE VALUE DIFFERS (CONVERGES AGAINST 0) BUT THE FORMULA IS CONSTANT.
	 * 	x_{k} - x_{k-1} = x^k / k! = \sum_{i=1}^{k}{x^i/i!} - \sum_{i=1}^{k-1}{i^k/i!} = x^k/k! + \sum_{k=1}^{i-1}{x^i/i!} - \sum_{i=1}^{i-1}{x^i/i!}
	 */
	public static double expApproximate_straightforward(double x, double eps)
	{
		int k = 1;								///< starting index, we start with 1 since there is no x_{-1} prior to x_0, we thus do an initial branching in the do-while loop below

		double x_k = x;							///< any current element of the sequence of expSums 			// the assignment is superfluous
		double x_prev;							///< the element prior to x_k

		do {									// evaluate the body at least once
			if(k == 1)							// to comply with the summation formula in task (f) where iteration steps in at i==0, cf. first comment in this method
				x_prev = expSum(x, k-1);		// this is one-time only!
			else
				x_prev = x_k;					// take previous x_k before it gets assigned to in the next step

			x_k = expSum(x, k++);				// note how k gets implicitly postscript-incremented (i.e., after evaluating k, that is, first read, then increment)
		} while(Math.abs(x_k - x_prev) > eps);	///< the inverted (for the loop to take place) convergence criterion as stated in the assignment (task f)

		System.out.println(red+"\tx_k == "+white+ x_k +red+", x_prev == "+white+ x_prev +off);

		System.out.println(blue+"Approximation complete. k == "+magenta+ (k-1) +off);

		return x_k;
	}


	/**
	 *	Teilaufgabe g.
	 * 		Falls gegen eine vorgegeben Konstante abgeglichen wird,
	 *			muss n mindestens
	 * 			*  7 betragen bei x == 1 und
	 * 			* 14 betragen bei x == 3.1416,
	 * 		Falls nach der Differenz von je zwei Nachfolgerelementen in einer Reihe verfahren wird,
	 * 			muss n mindestens
	 * 			*  8 betragen bei x == 1 und
	 * 			* 15 betragen bei x == 3.1416.
	 *
	 * 		-	Bis auf vier Ziffern genau ist die Approximation dann, wenn es kein größeres x_k gibt, für das sich die vierte Dezimalstelle ändert.
	 * 			Wenn also für x_k keine Änderung, dann ist die Approximation bereits bei x_k-1 erreicht worden.
	 * 			Es muss jedoch bis x_k geprueft werden, ansonsten bleibt es unbekannt, ob die so eingeschraenkte Differenz (auf vier Dezimalstellen) Null ist.
	 * 		-	Etwas vereinfacht, allerdings mit einer weiteren Voraussetzung, wird es nachfolgend geprüft, indem gegen einen fixen Wert abgegelichen wird.
	 * 			Die nachfolgende Funktion enthaelt beide Methoden, gibt jedoch nur den Wert der ersteren zurueck, weil sie dem Geist der Aufgaben entspricht, vgl. expApproximate.
	 * 			Wenn diese zusaetzliche Bedingung zulaessig ist, erspart diese Implementierung einen Schritt.
	 *
	 * 	@param x	cf. expApproximate
	 * 	@param cmp	an extra parameter to compare against--a fixed value with the expected precision of the decimals (four valid decimals)
	 * 	@return 	the least feasible number of iterations required for an approximation of up to four valid decimals
	 */
	public static int leastTop(double x, double cmp)
	{
		//	METHOD ONE:	compare against the difference of two consequent elements in a sequence (efficient computation of the difference)
		int k = 0;								///< starting index, we start with 1 since there is no x_{-1} prior to x_0, we thus do an initial branching in the do-while loop below

		while(power(x, k) / factorial(k++) > 0.0001);
		System.out.println(cyan+"\tMETHOD ONE: "+red+"\tx_k == "+white+ Math.floor(10000 * expSum(x, k-1))/10000 +green+ ", k == "+ (k-1) +off);

		//	METHOD TWO:	compare against the difference of two consequent elements in a sequence
		k = 1;													///< same as in expApproximate
		double x_k = x;											///< same as in expApproximate
		double x_prev;											///< same as in expApproximate

		do {													/// same as in expApproximate
			if(k == 1)											// NOTE only the conditional in while differs!
				x_prev = expSum(x, k-1);						// there we want to know whether the difference at the fourth decimal between any two consecutive sequence members equals zero, i.e.,
			else												// whether, with the ongoing iteration, the fourth decimal has stopped changing, which is precisely the case when
				x_prev = x_k;									// 			floor ( difference*10^4 ) == 0

			x_k = expSum(x, k++);
// 			System.out.println("k: "+ (k-1) +", x_k: "+ x_k +", x_prev"+ x_prev);
		} while(Math.floor(10000 * (x_k - x_prev)) != 0);		// we don't have to divide by 10,000 after computing the floor since we exactly want to know whether the difference at the fourth decimal == 0

		System.out.println(cyan+"\tMETHOD TWO: "+red+"\tx_k == "+white+ Math.floor(10000 * x_k)/10000 +green+ ", k == "+ (k-1) +off);
		// if we go with this METHOD ONE, then return k-1 since we started at k==1
// 		return k-1;


		//	METHOD THREE: 	compare against a constant value desired to approximate for
		k = 0;
		while((double) Math.floor(10000 * expSum(x, k++)) / 10000 != cmp){		// just this one condition in the loop (iteration over k):
																				// 1. multiply every k-th expSum by 10,000 and round down (floor -> long) to cut all the decimals,
																				// 2. then convert it to double and divide again by 10,000 as a double
																				// 3. to get only the four first decimals of the original number.
																				// 4. compare this with the parameter cmp
// 			System.out.println("k: "+ k +", x_k: "+expSum(x, k));
		}

		System.out.println(cyan+"\tMETHOD THREE: "+red+"\tx_k == "+white+ (double) Math.floor(10000 * expSum(x, k-1)) / 10000 +green+ ", k == "+ (k-1) +off);
		// if METHOD 2, then return k
		return k;
	}
	/** Task h) has been omitted in this branch. See local repo. */
};
