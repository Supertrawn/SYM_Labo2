package heig_vd.sym_labo2.communication;

/**
 * @Class       : CommunicationEventListener
 * @Author(s)   : Michael Brouchoud, Thomas Lechaire & Kevin Pradervand
 * @Date        : 16.11.2018
 *
 * @Goal        : Listen the event to process the response received from the server
 *
 * @Comment(s)  : -
 */
public interface CommunicationEventListener {
    /**
     * @brief Process the response to display it to the user interface
     * @param response The response to process
     */
    void handleServerResponse(String response);
}
