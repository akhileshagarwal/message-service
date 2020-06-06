package com.optima.message.rest;


import com.optima.message.data.MessageDataManager;
import com.optima.message.dto.Message;
import com.optima.message.exception.IncorrectIndexesException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@RestController
public class MessageServiceController {

    private MessageDataManager messageDataManager;

    private static final Logger LOG = Logger.getLogger(MessageServiceController.class.getName());

    private Pattern indexPattern = Pattern.compile("[0-9]+-[0-9]+");

    public MessageServiceController(MessageDataManager messageDataManager) {
        this.messageDataManager = messageDataManager;
    }

    @PostMapping("/message")
    public void deliver(@RequestBody Message message) {
        LOG.info("Entered into deliver");
        messageDataManager.deliverMessageToMail(message);
    }

    @GetMapping("/messages/{email:.+}")
    public List<String> fetch(@RequestParam(value = "indexes", defaultValue = "0-9") String indexes,
                              @RequestParam(value = "unFetched", defaultValue = "false") boolean unFetched,
                              @PathVariable String email) {
        LOG.info("Entered into fetch with");
        validateIndex(indexes);

        if (unFetched) {
            LOG.info("Entered into fetch with un-fetched flag");
            return messageDataManager.getUnFetchedMessages(email);
        } else {
            LOG.info("Entered into fetch with index search");
            return messageDataManager.getMessagesByRange(email, indexes);
        }

    }

    @DeleteMapping("/messages/{email:.+}/{indexes}")
    public List<String> delete(@PathVariable String email, @PathVariable String indexes) {
        LOG.info("Entered into delete");
        validateIndex(indexes);
        return messageDataManager.deleteMessages(email, indexes);
    }

    private void validateIndex(String indexes) {
        if (!indexPattern.matcher(indexes).matches()) {
            throw new IncorrectIndexesException("Incorrect Indexes");
        }
    }

}
