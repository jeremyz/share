
import java.util.Random;

abstract class CommonState implements StateLessStateMachine.MachineState
{
    protected void say()
    {
        System.out.println(" # " + this.getClass().getName());
    }
}

class StartState extends CommonState
{
    public StateLessStateMachine.State process()
    {
        say();
        System.out.println("   GO!");
        return StateLessStateMachine.State.TRY;
    }
}

class TryState extends CommonState
{
    public StateLessStateMachine.State process()
    {
        StateLessStateMachine.State nextState = null;

        say();

        int i = (new Random()).nextInt(6);
        switch (i) {
            case 1:
                System.out.println("   -> Success");
                nextState = StateLessStateMachine.State.SUCCESS;
                break;
            default:
                System.out.println("   -> Failure");
                nextState = StateLessStateMachine.State.FAILURE;
                break;
        }

        return nextState;
    }
}

class FailureState extends CommonState
{
    public StateLessStateMachine.State process()
    {
        say();
        System.out.println("   -> Retry");
        return StateLessStateMachine.State.TRY;
    }
}

class SuccessState extends CommonState
{
    public StateLessStateMachine.State process()
    {
        say();
        System.out.println("   -> well done");
        return StateLessStateMachine.State.STOP;
    }
}

class StateLessStateMachine
{
    public enum State
    {
        STOP(-1), START(0), TRY(1), SUCCESS(2), FAILURE(3);
        public int i;
        State(int i) { this.i = i; }
    }

    interface MachineState
    {
        public State process();
    }

    private MachineState[] states = {
        new StartState(),
        new TryState(),
        new SuccessState(),
        new FailureState()
    };

    public State process(State currentState)
    {
        if (currentState == State.STOP) return currentState;
        return states[currentState.i].process();
    }
}

public class StateLessStateMachineTest
{
    public static void main(String [] args )
    {
        System.out.println(StateLessStateMachineTest.class.getName());
        StateLessStateMachine stm = new StateLessStateMachine();

        StateLessStateMachine.State st = StateLessStateMachine.State.START;
        do {
            st = stm.process(st);
        } while (st != StateLessStateMachine.State.STOP);
    }
}
