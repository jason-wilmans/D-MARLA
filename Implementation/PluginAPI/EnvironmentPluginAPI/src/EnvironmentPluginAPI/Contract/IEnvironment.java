package EnvironmentPluginAPI.Contract;

import EnvironmentPluginAPI.Contract.Exception.CorruptMapFileException;
import EnvironmentPluginAPI.Contract.Exception.IllegalNumberOfClientsException;
import EnvironmentPluginAPI.Contract.Exception.TechnicalException;
import EnvironmentPluginAPI.TransportTypes.TMARLAClientInstance;
import EnvironmentPluginAPI.TransportTypes.TMapMetaData;

import java.util.List;

/**
 * Every environment hosted in MARLA must implement this interface. Environments provide the ability to react to actions of
 * AgentSystems and change their states.
 *
 * For every new cycle a new IEnvironment will be instantiated. The clients carry out their actions in a order determined
 * by the IEnvironment.
 * For every action a client carries out, the environment will change. The clients will carry out their actions, until
 * the IEnvironment terminates the cycle. This will happen, if a predetermined goal or some other termination condition
 * is met.
 *
 * If the IEnvironment implements no termination-condition, the cycle will (theoretically) go on forever, unless terminated
 * by the user.
 */
public interface IEnvironment {

    // TODO: TMapMetadata is too specific for generic Environments.
    /**
     * Returns a list of all saved maps.
     *
     * @return empty, if no maps found
     * @throws CorruptMapFileException if a map file, that was being tried to read, was somehow corrupted
     * @throws TechnicalException if any technical error's occured, that couldn't be handled
     */
    public List<TMapMetaData> getAvailableMaps() throws CorruptMapFileException, TechnicalException;

    /**
     * Saves a map. If it already exists, it will be overwritten.
     *
     * @param map the map to save != null
     */
    public void saveMap(TMapMetaData map) throws TechnicalException;

    /**
     * Starts the environment with the given parameters.
     *
     * @param marlaClients a list of all marla networkClients that will take part in the environment session
     * @param map (optionally) metadata for the map generation
     * @return an object describing the environment' state when the game starts, != null
     * @throws TechnicalException if any technical error's occured, that couldn't be handled
     */
    public IEnvironmentState start(List<TMARLAClientInstance> marlaClients, TMapMetaData map) throws TechnicalException, IllegalNumberOfClientsException;

    /**
     * Denotes if the current environment is still active. If true, the MARLA system will use the value of
     * getActiveInstance() to ask that client for his next turn. If false, the MARLA system will stop letting the
     * clients make turns and save a replay of this session.
     * @return see description
     */
    public boolean isStillActive();

    /**
     * Marks, which instance of a MARLA-Client was chosen to make the next turn.
     * <br/><br/>
     * The client, that is returned here, will receive this environment state. Its answer, in form of an
     * IActionDescription, will be the next input for the environment.
     *
     * @see EnvironmentPluginAPI.Contract.IActionDescription
     * @see EnvironmentPluginAPI.TransportTypes.TMARLAClientInstance
     * @return must be part of this environment, != null
     */
    public TMARLAClientInstance getActiveInstance();

    /**
     * Gets the current state of the map for the running game.
     *
     * @return The current state of the game. != null
     * @see IEnvironmentState
     */
    IEnvironmentState getCurrentGameState() throws TechnicalException;

    /**
     * Executes the action described by the actionDescription. The environment will change accordingly, and an updated
     * environment-state will be returned.
     * @param actionDescription an object describing the action(s) taken by the marla client
     * @return The state of the environment after the action has been executed, != null
     * @see IActionDescription
     */
    IEnvironmentState executeAction(IActionDescription actionDescription) throws TechnicalException;

    /**
     * Explicitly ends the turn of the active MARLAClient.
     *
     * @throws TechnicalException
     */
    void endTurn() throws TechnicalException;

    /**
     * Called, when MARLA is about to end the current environment session. Can be used for cleanup and/or saving replays.
     */
    public void end() throws TechnicalException;
}
