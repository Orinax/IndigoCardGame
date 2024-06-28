package indigo

const val DIAMONDS = "\u2666"
const val HEARTS = "\u2665"
const val SPADES = "\u2660"
const val CLUBS = "\u2663"
const val ONE_HAND = 6

val cardRanks = listOf<Any>('A', '2', '3', '4', '5', '6', '7', '8', '9', "10", 'J', 'Q', 'K')
val cardSuits = listOf(DIAMONDS, HEARTS, SPADES, CLUBS)

class CardDeck(cardRanks: List<Any>, cardSuits: List<String>) {
    var cardsInDeck = mutableListOf<String>()
    init {
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                // print("$rank$suit ")
                cardsInDeck.add("$rank$suit")
            }
        }
    }

    fun reset() {
        cardsInDeck.clear()
        for (suit in cardSuits) {
            for (rank in cardRanks) {
                cardsInDeck.add("$rank$suit")
            }
        }
        println("Card deck is reset.")
    }

    fun shuffleDeck() {
        for ((deckIndex, card) in cardsInDeck.shuffled().withIndex()) {
            cardsInDeck[deckIndex] = card
        }
        println("Card deck is shuffled.")
    }

    fun removeCardsFromDeck(cardsToRemove: Int): List<String> {
        // Completely refactored because players will not be asking for specific numbers of cards.
        // println("Number of cards:")
        // Using toIntoOrNull to handle user input that can't be cast to Int.
        // When cardsToGet can't be converted to Int, a default value of -1 is set so the else block can be reached.
        // val cardsToGet = readln().toIntOrNull() ?: -1
//        if ((cardsToDeal > 0) && (cardsToDeal < 53)) {
//            if (cardsToDeal > cardsInDeck.size) {
//                println("The remaining cards are insufficient to meet the request.")
//            } else {
                val takenCards = cardsInDeck.take(cardsToRemove)
                cardsInDeck.removeAll(cardsInDeck.take(cardsToRemove))
                return takenCards
                // No longer have to display taken cards as they go to the table or a player.
                // for (card in takenCards) {
                //    print("$card ")
                // }
//            }
//        } else {
//            println("Invalid number of cards.")
//        }
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
    private var identifiedFirstPlayer = false

    fun printGameTitle() {
        println("Indigo Card Game")
    }

    fun decideFirstPlayer(playerOne: Player, playerTwo: Player) {
        while (!identifiedFirstPlayer) {
            println("Play first?")
            val playerAnswer = readln()
            if (playerAnswer.lowercase() == "yes") {
                playerOne.isFirstPlayer = true
                playerTwo.isFirstPlayer = false
                identifiedFirstPlayer = true
            } else if (playerAnswer.lowercase() == "no") {
                playerOne.isFirstPlayer = false
                playerTwo.isFirstPlayer = true
                identifiedFirstPlayer = true
            }
        }
    }

    fun displayActionMenu(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        println("Choose an action (reset, shuffle, get, exit):")
        val playerChoice = readln()
        if (playerChoice == "reset") {
            deck.reset()
        } else if (playerChoice == "shuffle") {
            deck.shuffleDeck()
        } else if (playerChoice == "get") {
            deck.removeCardsFromDeck(0)
        } else if (playerChoice == "exit") {
            keepPlaying = exitGame()
        } else if (playerChoice == "look") {
            lookAtCards(deck, table, playerOne, playerTwo)
        } else {
            println("Wrong action.")
        }
    }

    fun dealCards(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        table.cardsOnTable.addAll(deck.removeCardsFromDeck(4))
        playerOne.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
        playerTwo.cardsInHand.addAll(deck.removeCardsFromDeck(ONE_HAND))
    }

    private fun lookAtCards(deck: CardDeck, table: Table, playerOne: Player, playerTwo: Player) {
        // Display all cards currently in play.
        print("Cards remaining in the deck: ")
        displayCards(deck.cardsInDeck)
        print("Cards on the table: ")
        displayCards(table.cardsOnTable)
        print("Cards in Player One's hand: ")
        displayCards(playerOne.cardsInHand)
        print("Cards in Player Two's hand: ")
        displayCards(playerTwo.cardsInHand)
    }

    private fun displayCards(cards: MutableList<String>) {
        for (card in cards) {
            print("$card ")
        }
        println()
    }

    private fun exitGame(): Boolean {
        println("Bye")
        return true
    }
}

class Player() {
    var cardsInHand = mutableListOf<String>()
    var isFirstPlayer = true
}

class Table() {
    var cardsOnTable = mutableListOf<String>()
    private val startingTableCards = 4
}

fun main() {
    val cardDeck = CardDeck(cardRanks, cardSuits)
    val playerOne = Player()
    val playerTwo = Player()
    val table = Table()
    val gameHandler = GameHandler()

    gameHandler.printGameTitle()
    gameHandler.decideFirstPlayer(playerOne, playerTwo)
    gameHandler.dealCards(cardDeck, table, playerOne, playerTwo)

    while (!gameHandler.keepPlaying) {

        gameHandler.displayActionMenu(cardDeck, table, playerOne, playerTwo)
        println()
    }
}