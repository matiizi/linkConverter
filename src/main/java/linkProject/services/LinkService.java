package linkProject.services;

import jakarta.transaction.Transactional;
import linkProject.models.Link;
import linkProject.repozitories.LinkRepo;
import linkProject.repozitories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

@Service
public class LinkService {

    @Autowired
    LinkRepo linkRepo;
    @Autowired
    UserRepo userRepo;
    

    public LinkService () {}

    private long linkLifetime = 20 * 1000;

    public Link createShortLink(String longUrl, long user_id, int clickLimit) {
        String shortURl = "";
        long expirationTime = 0;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest((longUrl + System.currentTimeMillis()).getBytes());

            for (byte b : hash) {
                shortURl += Integer.toHexString(0xff & b);
            }
            shortURl = shortURl.substring(0, 6);
            shortURl = "clck.ru/" + shortURl;
            expirationTime = System.currentTimeMillis() + linkLifetime;
        }
        catch (Exception e) {
            System.out.println("Error generating short url");
        }

        return new Link(shortURl, longUrl, 0, clickLimit, expirationTime, user_id);
    }

    public Link findById(long id) {
        return linkRepo.findById(id).orElseThrow();
    }

    public List<Link> getUserLinks(long userId) {
        return linkRepo.findByUserId(userId);
    }

    @Scheduled(fixedRate = 30000)
    @Transactional
    public void deleteAllExpiredLinks() {
//        linkRepo.deleteByExpirationTimeLessThan(System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        List<Link> expiredLinks = linkRepo.findExpiredLinks(currentTime);

        if (!expiredLinks.isEmpty()) {
            expiredLinks.stream()
                    .map(Link::getUserId)
                    .distinct()
                    .forEach(userId -> {
                        userRepo.findById(userId).ifPresent(user -> {
                            user.setDeletedLinksCount(user.getDeletedLinksCount() + 1);
                            userRepo.save(user);
                        });
                    });
            linkRepo.deleteAll(expiredLinks);
        }
    }

    public Optional<String> redirect(String shortUrl) {
        Optional<Link> linkOpt = linkRepo.findByShortUrl(shortUrl);
        if (linkOpt.isEmpty()) {
            return Optional.empty();
        }
        Link link = linkOpt.get();
        if (link.getExpirationTime() < System.currentTimeMillis()) {
            deleteAllExpiredLinks();
            return Optional.of("Error: Expired");
        }
        if (link.getClickCount() >= link.getClickLimit()) {
            return Optional.of("Error: Limit of clicks");
        }

        link.setClickCount(link.getClickCount() + 1);
        linkRepo.save(link);
        return Optional.of(link.getLongUrl());
    }

    public void save(Link link) {
        linkRepo.save(link);
    }
}
