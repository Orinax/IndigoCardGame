package indigo

const val DIAMONDS = "\u2666"
const val HEARTS = "\u2665"
const val SPADES = "\u2660"
const val CLUBS = "\u2663"

val cardRanks = listOf<Any>('A', '2', '3', '4', '5', '6', '7', '8', '9', "10", 'J', 'Q', 'K')
val cardSuits = listOf(DIAMONDS, HEARTS, SPADES, CLUBS)

class CardDeck(cardRanks: List<Any>, cardSuits: List<String>) {
    private var deck = mutableListOf<String>()
    init {
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                // print("$rank$suit ")
                deck.add("$rank$suit")
            }
        }
    }

    fun shuffleDeck() {
        for ((deckIndex, card) in deck.shuffled().withIndex()) {
            deck[deckIndex] = card
        }
        println("Card deck is shuffled.")
    }

    fun getCards() {
        println("Number of cards:")
        val cardsToGet = readln().toIntOrNull() ?: -1
        if ((cardsToGet > 0) && (cardsToGet < 53)) {
            if (cardsToGet > deck.size) {
                println("The remaining cars are insufficient to meet the request.")
            } else {
                var cardsInHand = deck.take(cardsToGet)
                deck.removeAll(deck.take(cardsToGet))
                for (card in cardsInHand) {
                    print("$card ")
                }
            }
        } else {
            println("Invalid number of cards.")
        }
    }

    fun displayCardsInDeck() {
        // Display all cards currently in the deck.
        for (card in deck) {
            print("$card ")
        }
    }

    fun displayRanks() {
        // Print all ranks
        for (rank in cardRanks) {
            print("$rank ")
        }
        println()
    }

    fun displaySuits() {
        // Print all suits.
        for (suit in cardSuits) {
            print("$suit ")
        }
        println()
    }

    fun displayShuffledDeck() {
        for (suit in cardSuits.shuffled()) {
            for (rank in cardRanks.shuffled()) {
                print("$rank$suit ")
            }
        }
    }
}

class GameHandler() {
    var keepPlaying = false

    fun displayActionMenu(deck: CardDeck) {
        println("Choose an action (reset, shuffle, get, exit):")
        val playerChoice = readln()
        if (playerChoice == "reset") {

        } else if (playerChoice == "shuffle") {
            deck.shuffleDeck()
        } else if (playerChoice == "get") {
            deck.getCards()
        } else if (playerChoice == "exit") {
            keepPlaying = exitGame()
        } else if (playerChoice == "look") {
            deck.displayCardsInDeck()
        } else {
            println("Wrong action")
        }
    }

    private fun exitGame(): Boolean {
        return true
    }
}


fun main() {
    val cardDeck = CardDeck(cardRanks, cardSuits)
    val gameHandler = GameHandler()

    while (!gameHandler.keepPlaying) {
        gameHandler.displayActionMenu(cardDeck)
        println()
    }
}