package indigo

const val DIAMONDS = "\u2666"
const val HEARTS = "\u2665"
const val SPADES = "\u2660"
const val CLUBS = "\u2663"



class cardDeck() {
    private val cardRanks = listOf('A', '2', '3', '4', '5', '6', '7', '8', '9', "10", 'J', 'Q', 'K')
    private val cardSuits = listOf(DIAMONDS, HEARTS, SPADES, CLUBS)

    fun displayActionMenu() {
        println("Choose an action (reset, shuffle, get, exit):")
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


fun main() {
    val cardDeck = cardDeck()
    cardDeck.displayRanks()
    cardDeck.displaySuits()
    cardDeck.displayShuffledDeck()
    cardDeck.displayActionMenu()
}