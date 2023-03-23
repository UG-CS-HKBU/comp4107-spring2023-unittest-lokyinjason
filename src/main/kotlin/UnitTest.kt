import junit.framework.TestCase.assertTrue
import org.junit.Test

class UnitTest {
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = CaoCao()
        for (i in 0 until 7) {
            heroes.add(NoneMonarchFactory.createRandomHero())
            println(heroes[i].name + ": " + heroes[i].roleTitle)
        }
        println()
        assertTrue(monarchHero.dodgeAttack())
    }

    @Test
    fun testBeingAttacked() {
        if (heroes.size == 0) {
            for (i in 0 until 7) {
                heroes.add(NoneMonarchFactory.createRandomHero())
                println(heroes[i].name + ": " + heroes[i].roleTitle)
            }
            println()
        }

        for (i in 0 until heroes.size) {
            val spy = object: WarriorHero(MinisterRole()) {
                override val name = heroes[i].name
                override fun beingAttacked() {
                    heroes[i].beingAttacked()
                    assertTrue(heroes[i].hp >= 0)
                }
            }

            for (i in 0 until 10) spy.beingAttacked()
            println()
        }
    }

    object FakeNonmonarchFactory: GameObjectFactory {
        var count = 0
        var last: WeiHero? = null
        override fun getRandomRole(): Role =
            MinisterRole()
        override fun createRandomHero(): Hero {
            val hero = when(count++) {
                0->SimaYi(getRandomRole())
                1 -> XuChu(getRandomRole())
                else->XiaHouyuan(getRandomRole())
            }

            if(count >= 2) count = 0

            if (monarchHero is CaoCao) {
                val cao = monarchHero as CaoCao
                if (cao.helper == null)
                    cao.helper = hero as WeiHero
                else {
                    var handler: Handler? = cao.helper as Handler

                    while (handler!!.hasNext())
                        handler = handler.getNext()
                    handler.setNext(hero as Handler)
                }
            }
            return hero
        }
    }

    object FakeMonarchFactory: GameObjectFactory {
        override fun getRandomRole(): Role =
            MonarchRole()
        override fun createRandomHero(): Hero {
            return CaoCao()
        }
    }

    class DummyRole: Role {
        override val roleTitle: String = "Dummy"
        override fun getEnemy() = "I am a dummy, I have no enemies."
    }

    @Test
    fun testDiscardCards() {
        val dummy = DummyRole()
        val hero = ZhangFei(dummy)

        hero.discardCards()
    }
}

class CaoCaoUnitTest {
    @Test
    fun testCaoDodgeAttack() {
        monarchHero = UnitTest.FakeMonarchFactory.createRandomHero() as MonarchHero
        heroes = mutableListOf<Hero>();

        heroes.add(monarchHero)
        monarchHero.setCommand(Abandon(monarchHero))
        for (i in 0..2) {
            var hero = UnitTest.FakeNonmonarchFactory.createRandomHero()
            hero.index = heroes.size;
            heroes.add(hero)
        }

        for (hero in heroes) {
            hero.beingAttacked()
            hero.templateMethod()
        }
        assertTrue(monarchHero.dodgeAttack())
    }
}