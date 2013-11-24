/* vi: set sw=2 ts=2 sts=2: */

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

#include <iostream>
// #include <random>			/// TODO use a Mersienne Twister instead or feed from /dev/urandom if sufficient entropy is feasible to expect

#include <stdlib.h>
#include <time.h>
#include <unistd.h>

# ifndef EXIT_FAILURE
#  define EXIT_FAILURE 1
# endif

using std::cout;
using std::cerr;
using std::cin;
using std::endl;

/// TODO a further abstraction for a variable number of players

namespace os
{
	using std::string;
	string off("\e[0;0m");
	string bold("\e[1;1m");
	string red("\e[1;31m");
	string green("\033[1;32m");
	string yellow("\033[1;33m");
	string blue("\e[1;34m");
	string magenta("\033[1;35m");
	string cyan("\033[1;36m");
	string white("\033[1;37m");
	string black("\033[1;30m");
	string darkwhite("\033[0;37m");
	string darkyellow = "\033[0;33m";
	string darkgreen = "\033[0;32m";
	string darkblue = "\033[0;34m";
	string darkcyan = "\033[0;36m";
	string darkred = "\033[0;31m";
	string darkmagenta = "\033[0;35m";
	string darkblack = "\033[0;30m";
};

using namespace os;


int main(int argc, char **argv)
{
	if(argc != 4 && argc != 5)
	{
		cerr 	<< "Die Anzahl von Argumenten ist falsch. Erwartete Befehlseingabe hat das folgende Format\n\t"
				<< argv[0] << " s m r, wobei \n"
				<< "\t\t s für die anfängliche Anzahl an Streichhölzern (Startmenge),\n"
				<< "\t\t m für die höchste (maximale) Anzahl an Streichhölzern, die bei einem Zug von dem Spieler entfernt werden dürfen, und\n"
				<< "\t\t r für den Rest steht, der Anzahl von Streichhölzern entspricht, dessen Vorliegen für den Spieler, der am Zuge ist, zur Niederlage führt.\n"
				<< "\t\tAlle Argumente werden als ganzzählig interpretiert.\n"
				<< endl;
		exit(EXIT_FAILURE);
	}

	int slp = 0;
	if(argc == 5)
		slp = atoi(argv[4]);

	srand(time(NULL));

	int m = atoi(argv[2]),
		r = atoi(argv[3]),
		l = atoi(argv[1]);

	int n = (l-r) / (m+1);

	cout << "Sie starten das Spiel mit " <<yellow<< l <<off<< " Streichhölzern, wobei bei jedem Zug höchstens " <<yellow<< m <<off<< " Streichhölzer entfernt werden dürfen und das Verbleiben von nur " <<yellow<< r <<off<< " Streichhölzern für den Spieler, der dabei am Zuge ist, die Niederlage bedeutet." << endl;
	cout <<bold<< "Wir sind am Zug." <<off<< endl;

	int anfang = r+n*(m+1);		// m+1 ist die summe von der oberen und der unteren schranke der moeglichen zahlen, die je zug entfern werden duerfen, da der gegner 1,2,3 ziehen koennte
	int summe = m+1;			/// TEST I assume that this 1 stands for the minimal number of matches to be taken, we can make a further abstraction then

	if(anfang == l)
		cout <<magenta<< "Bei dieser Variante verliert der Spieler, der zunächst am Zug ist, immer, falls der Gegenspieler der sicheren Strategie folgt." <<off<< endl;
	else
		cout <<cyan<< "Die erstmögliche Position, die den Gegener zur Niederlage führt, ist " <<yellow<< anfang <<cyan<< ", auf welche wir den Gegner sofort bringen." <<off<< endl;
	cout << "Unsere sichere Strategie lautet: " <<blue<< " Die Summe je zwei Züge muss stets " <<yellow<< summe <<blue<< " betragen." <<off<< endl;
	if(anfang > l) {
		cerr <<red<< "Unerwarterter Fehler: anfang > l." <<off<< endl;
		exit(EXIT_FAILURE);
	}

	int zug 	= (l - anfang) ? l-anfang : rand() % m + 1;	// if l-anfang == 0 that is, then we must take at least 1 match, let's take some pseudo-random integer here instead
	int rest 	= l - zug;

	sleep(slp);
	cout <<cyan<< "Spieler 1 folgt stets der sicheren Strategie." <<off<< endl;

	if(l == anfang)
		cout <<cyan<< "Spieler 1 kann dieses Spiel mit Sicherheit nicht gewinnen, falls Spieler 2 der sicheren Strategie folgt, deshalb soll dieser an dieser Stelle vorerst pseudo-zufällig die Anzahl der Streichölzer ziehen." <<off<< endl;

	cout <<green<< "Spieler 1 (wir)" <<off<<" zieht " <<yellow<< zug <<off<< " Streichhölzer. Es verbleiben " <<yellow<< rest <<off<< " Streichhölzer." <<off<< endl;

	if(l != anfang)
		cout <<cyan<< "Spieler 2 kann dieses Spiel nicht mehr gewinnen. Deshalb nehmen wir zunächst an, dass er pseudo-zufällig die Anzahl der Streichölzer zieht." <<off<< endl;

	bool spieler2 = true; 			// wer am Zug ist, erst #2==true
	std::string color(spieler2 ? red : green);

	sleep(slp);

	/// TODO multiple silent simulations

	while(rest != r)
	{
		if(spieler2)
			zug = rand() % m + 1;
		else
			zug = summe - zug;		// less the prior turn's number of matches the other player (#2) took

		if(rest <= r+m)
			zug = rest - r;

		rest -= zug;
		cout <<color<< "Spieler " << std::string(spieler2 ? "2" : "1") <<off<< " zieht " <<yellow<< zug <<off<< " Streichhölzer. Es verbleiben " <<yellow<< rest <<off<< " Streichhölzer." << endl;

		if(spieler2)				// player2 has made his move
			spieler2 = false;		// now it's the other player's (#1) turn
		else
			spieler2 = true;
		color = spieler2 ? red : green;

		sleep(slp);
	}
	cout << "Es sind nun " <<yellow<< rest <<off<< " Streichhölzer verblieben und" <<color<< " Spieler " << std::string(spieler2 ? "2" : "1") <<off<< " ist am Zug. Somit verliert er." << endl;

	/// TODO as a proof of concept: player2 takes a non-random number but like player1 -- follows the "summe"-strategy
	/// TODO an interactive play

	return 0;
}
