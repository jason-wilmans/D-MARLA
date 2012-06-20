package ServerRunner.Implementation;

import EnvironmentPluginAPI.Contract.Exception.TechnicalException;
import EnvironmentPluginAPI.Service.ISaveGameStatistics;
import EnvironmentPluginAPI.TransportTypes.TMARLAClientInstance;
import NetworkAdapter.Interface.Exceptions.ConnectionLostException;
import NetworkAdapter.Interface.IServerNetworkAdapter;
import PluginLoader.Interface.Exceptions.PluginNotReadableException;
import PluginLoader.Interface.IPluginLoader;
import ServerRunner.Interface.IPlayerEventHandler;
import ServerRunner.Interface.IServerRunner;
import ServerRunner.Interface.SessionIsNotInReadyStateException;
import TransportTypes.TNetworkClient;
import TransportTypes.TSession;

import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: N3trunner
 * Date: 20.05.12
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */
public class ServerRunnerComponent implements IServerRunner {

    ServerRunnerUseCase useCase;

    public ServerRunnerComponent(ISaveGameStatistics saveGameStatistics, IPluginLoader pluginLoader, IServerNetworkAdapter networkAdapter) {
        this.useCase = new ServerRunnerUseCase(saveGameStatistics, pluginLoader, networkAdapter);
    }

    @Override
    public void startHosting() throws TechnicalException, ConnectionLostException {
        useCase.startHosting();
    }

    @Override
    public void stopHosting() {
        useCase.stopHosting();
    }

    @Override
    public UUID createSession(TSession session) throws TechnicalException, PluginNotReadableException {
        return useCase.createSession(session);
    }

    @Override
    public void updateSession(UUID id, TSession session) throws SessionIsNotInReadyStateException {
        useCase.updateSession(id, session);
    }

    @Override
    public TSession getSessionById(UUID id) {
        return useCase.getSessionById(id);
    }

    @Override
    public List<TSession> getAllSessions() {
        return useCase.getAllSessions();
    }

    @Override
    public void startAllReadySessions() {
        useCase.startAllReadySessions();
    }

    @Override
    public List<TNetworkClient> getFreeClients() {
        return useCase.getFreeClients();
    }

    @Override
    public void subscribeForPlayerEvent(IPlayerEventHandler playerEventHandler) {
        useCase.subscribeForPlayerEvent(playerEventHandler);
    }

    @Override
    public List<TMARLAClientInstance> getConnectedPlayers() {
        return useCase.getConnectedPlayers();
    }
}