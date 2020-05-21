package it.polito.ai.esercitazione3.services;

import it.polito.ai.esercitazione3.dtos.TeamDTO;
import it.polito.ai.esercitazione3.entities.Token;
import it.polito.ai.esercitazione3.repositories.TeamRepository;
import it.polito.ai.esercitazione3.repositories.TokenRepositories;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
  @Autowired
  JavaMailSender emailSender;

  @Autowired
  TokenRepositories tokenRepo;

  @Autowired
  TeamRepository teamRepo;

  @Autowired
  TeamService teamService;

  private final AtomicBoolean error = new AtomicBoolean(false);

  @Value("${service.sendTo}")
  private String sendTo;

  @Scheduled(fixedRate = 600000)
  public void reportCurrentTime() {
    Date date = new Date();
    tokenRepo
      .findAllByExpiryDateBefore(new Timestamp(date.getTime()))
      .forEach(
        token -> {
          tokenRepo.delete(token);
          teamService.evictTeam(token.getTeamId());
        }
      );
  }

  @Override
  public void sendMessage(String address, String subject, String body) {
    MimeMessage mimeMessage = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    try {
      helper.setText(messageBuilder(body), true);
      helper.setTo(address);
      helper.setSubject(subject);
      emailSender.send(mimeMessage);
    } catch (MessagingException e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean confirm(String token) {
    Date date = new Date();
    boolean expired = tokenRepo
      .findAllByExpiryDateBefore(new Timestamp(date.getTime()))
      .stream()
      .anyMatch(t -> t.getId().equals(token));
    if (expired || !tokenRepo.existsById(token)) {
      error.set(true);
      return false;
    }
    Long id = tokenRepo.getOne(token).getTeamId();
    tokenRepo.deleteById(token);
    error.set(false);
    if (tokenRepo.findAllByTeamId(id).size() <= 0) {
      return teamService.setActive(id);
    }
    return false;
  }

  @Override
  public boolean reject(String token) {
    Date date = new Date();
    boolean expired = tokenRepo
      .findAllByExpiryDateBefore(new Timestamp(date.getTime()))
      .stream()
      .anyMatch(t -> t.getId().equals(token));
    if (expired || !tokenRepo.existsById(token)) return false;
    Long id = tokenRepo.getOne(token).getTeamId();
    tokenRepo
      .findAllByTeamId(id)
      .forEach(
        t -> {
          tokenRepo.delete(t);
        }
      );
    return teamService.evictTeam(id);
  }

  @Override
  public void notifyTeam(TeamDTO team, List<String> membersIds) {
    Date date = new Date();
    Timestamp expire = new Timestamp(DateUtils.addHours(date, 1).getTime());
    membersIds.forEach(
      id -> {
        String token = UUID.randomUUID().toString();
        Token memberToken = new Token();
        memberToken.setId(token);
        memberToken.setExpiryDate(expire);
        memberToken.setTeamId(team.getId());
        tokenRepo.save(memberToken);

        String email = "s" + id + "@studenti.polito.it";
        String body =
          "Accept by click here : http://localhost:8080/notification/confirm/" +
          token +
          "<br>" +
          "Decline by click here:http://localhost:8080/notification/reject/" +
          token;

        this.sendMessage(sendTo, "Team Propose", body);
      }
    );
  }

  private String messageBuilder(String message) {
    return (
      "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
      "\n" +
      "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
      "<head>\n" +
      "<!--[if gte mso 9]><xml><o:OfficeDocumentSettings><o:AllowPNG/><o:PixelsPerInch>96</o:PixelsPerInch></o:OfficeDocumentSettings></xml><![endif]-->\n" +
      "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
      "<meta content=\"width=device-width\" name=\"viewport\"/>\n" +
      "<!--[if !mso]><!-->\n" +
      "<meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\"/>\n" +
      "<!--<![endif]-->\n" +
      "<title></title>\n" +
      "<!--[if !mso]><!-->\n" +
      "<link href=\"https://fonts.googleapis.com/css?family=Lato\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
      "<!--<![endif]-->\n" +
      "<style type=\"text/css\">\n" +
      "\t\tbody {\n" +
      "\t\t\tmargin: 0;\n" +
      "\t\t\tpadding: 0;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\ttable,\n" +
      "\t\ttd,\n" +
      "\t\ttr {\n" +
      "\t\t\tvertical-align: top;\n" +
      "\t\t\tborder-collapse: collapse;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t* {\n" +
      "\t\t\tline-height: inherit;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\ta[x-apple-data-detectors=true] {\n" +
      "\t\t\tcolor: inherit !important;\n" +
      "\t\t\ttext-decoration: none !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser table {\n" +
      "\t\t\ttable-layout: fixed;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t[owa] .img-container div,\n" +
      "\t\t[owa] .img-container button {\n" +
      "\t\t\tdisplay: block !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t[owa] .fullwidth button {\n" +
      "\t\t\twidth: 100% !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t[owa] .block-grid .col {\n" +
      "\t\t\tdisplay: table-cell;\n" +
      "\t\t\tfloat: none !important;\n" +
      "\t\t\tvertical-align: top;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid,\n" +
      "\t\t.ie-browser .num12,\n" +
      "\t\t[owa] .num12,\n" +
      "\t\t[owa] .block-grid {\n" +
      "\t\t\twidth: 650px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .mixed-two-up .num4,\n" +
      "\t\t[owa] .mixed-two-up .num4 {\n" +
      "\t\t\twidth: 216px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .mixed-two-up .num8,\n" +
      "\t\t[owa] .mixed-two-up .num8 {\n" +
      "\t\t\twidth: 432px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.two-up .col,\n" +
      "\t\t[owa] .block-grid.two-up .col {\n" +
      "\t\t\twidth: 324px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.three-up .col,\n" +
      "\t\t[owa] .block-grid.three-up .col {\n" +
      "\t\t\twidth: 324px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.four-up .col [owa] .block-grid.four-up .col {\n" +
      "\t\t\twidth: 162px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.five-up .col [owa] .block-grid.five-up .col {\n" +
      "\t\t\twidth: 130px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.six-up .col,\n" +
      "\t\t[owa] .block-grid.six-up .col {\n" +
      "\t\t\twidth: 108px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.seven-up .col,\n" +
      "\t\t[owa] .block-grid.seven-up .col {\n" +
      "\t\t\twidth: 92px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.eight-up .col,\n" +
      "\t\t[owa] .block-grid.eight-up .col {\n" +
      "\t\t\twidth: 81px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.nine-up .col,\n" +
      "\t\t[owa] .block-grid.nine-up .col {\n" +
      "\t\t\twidth: 72px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.ten-up .col,\n" +
      "\t\t[owa] .block-grid.ten-up .col {\n" +
      "\t\t\twidth: 60px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.eleven-up .col,\n" +
      "\t\t[owa] .block-grid.eleven-up .col {\n" +
      "\t\t\twidth: 54px !important;\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t.ie-browser .block-grid.twelve-up .col,\n" +
      "\t\t[owa] .block-grid.twelve-up .col {\n" +
      "\t\t\twidth: 50px !important;\n" +
      "\t\t}\n" +
      "\t</style>\n" +
      "<style id=\"media-query\" type=\"text/css\">\n" +
      "\t\t@media only screen and (min-width: 670px) {\n" +
      "\t\t\t.block-grid {\n" +
      "\t\t\t\twidth: 650px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid .col {\n" +
      "\t\t\t\tvertical-align: top;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid .col.num12 {\n" +
      "\t\t\t\twidth: 650px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.mixed-two-up .col.num3 {\n" +
      "\t\t\t\twidth: 162px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.mixed-two-up .col.num4 {\n" +
      "\t\t\t\twidth: 216px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.mixed-two-up .col.num8 {\n" +
      "\t\t\t\twidth: 432px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.mixed-two-up .col.num9 {\n" +
      "\t\t\t\twidth: 486px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.two-up .col {\n" +
      "\t\t\t\twidth: 325px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.three-up .col {\n" +
      "\t\t\t\twidth: 216px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.four-up .col {\n" +
      "\t\t\t\twidth: 162px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.five-up .col {\n" +
      "\t\t\t\twidth: 130px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.six-up .col {\n" +
      "\t\t\t\twidth: 108px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.seven-up .col {\n" +
      "\t\t\t\twidth: 92px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.eight-up .col {\n" +
      "\t\t\t\twidth: 81px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.nine-up .col {\n" +
      "\t\t\t\twidth: 72px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.ten-up .col {\n" +
      "\t\t\t\twidth: 65px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.eleven-up .col {\n" +
      "\t\t\t\twidth: 59px !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid.twelve-up .col {\n" +
      "\t\t\t\twidth: 54px !important;\n" +
      "\t\t\t}\n" +
      "\t\t}\n" +
      "\n" +
      "\t\t@media (max-width: 670px) {\n" +
      "\n" +
      "\t\t\t.block-grid,\n" +
      "\t\t\t.col {\n" +
      "\t\t\t\tmin-width: 320px !important;\n" +
      "\t\t\t\tmax-width: 100% !important;\n" +
      "\t\t\t\tdisplay: block !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.block-grid {\n" +
      "\t\t\t\twidth: 100% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.col {\n" +
      "\t\t\t\twidth: 100% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.col>div {\n" +
      "\t\t\t\tmargin: 0 auto;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\timg.fullwidth,\n" +
      "\t\t\timg.fullwidthOnMobile {\n" +
      "\t\t\t\tmax-width: 100% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col {\n" +
      "\t\t\t\tmin-width: 0 !important;\n" +
      "\t\t\t\tdisplay: table-cell !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack.two-up .col {\n" +
      "\t\t\t\twidth: 50% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col.num4 {\n" +
      "\t\t\t\twidth: 33% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col.num8 {\n" +
      "\t\t\t\twidth: 66% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col.num4 {\n" +
      "\t\t\t\twidth: 33% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col.num3 {\n" +
      "\t\t\t\twidth: 25% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col.num6 {\n" +
      "\t\t\t\twidth: 50% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.no-stack .col.num9 {\n" +
      "\t\t\t\twidth: 75% !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.video-block {\n" +
      "\t\t\t\tmax-width: none !important;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.mobile_hide {\n" +
      "\t\t\t\tmin-height: 0px;\n" +
      "\t\t\t\tmax-height: 0px;\n" +
      "\t\t\t\tmax-width: 0px;\n" +
      "\t\t\t\tdisplay: none;\n" +
      "\t\t\t\toverflow: hidden;\n" +
      "\t\t\t\tfont-size: 0px;\n" +
      "\t\t\t}\n" +
      "\n" +
      "\t\t\t.desktop_hide {\n" +
      "\t\t\t\tdisplay: block !important;\n" +
      "\t\t\t\tmax-height: none !important;\n" +
      "\t\t\t}\n" +
      "\t\t}\n" +
      "\t</style>\n" +
      "</head>\n" +
      "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #F5F5F5;\">\n" +
      "<style id=\"media-query-bodytag\" type=\"text/css\">\n" +
      "@media (max-width: 670px) {\n" +
      "  .block-grid {\n" +
      "    min-width: 320px!important;\n" +
      "    max-width: 100%!important;\n" +
      "    width: 100%!important;\n" +
      "    display: block!important;\n" +
      "  }\n" +
      "  .col {\n" +
      "    min-width: 320px!important;\n" +
      "    max-width: 100%!important;\n" +
      "    width: 100%!important;\n" +
      "    display: block!important;\n" +
      "  }\n" +
      "  .col > div {\n" +
      "    margin: 0 auto;\n" +
      "  }\n" +
      "  img.fullwidth {\n" +
      "    max-width: 100%!important;\n" +
      "    height: auto!important;\n" +
      "  }\n" +
      "  img.fullwidthOnMobile {\n" +
      "    max-width: 100%!important;\n" +
      "    height: auto!important;\n" +
      "  }\n" +
      "  .no-stack .col {\n" +
      "    min-width: 0!important;\n" +
      "    display: table-cell!important;\n" +
      "  }\n" +
      "  .no-stack.two-up .col {\n" +
      "    width: 50%!important;\n" +
      "  }\n" +
      "  .no-stack.mixed-two-up .col.num4 {\n" +
      "    width: 33%!important;\n" +
      "  }\n" +
      "  .no-stack.mixed-two-up .col.num8 {\n" +
      "    width: 66%!important;\n" +
      "  }\n" +
      "  .no-stack.three-up .col.num4 {\n" +
      "    width: 33%!important\n" +
      "  }\n" +
      "  .no-stack.four-up .col.num3 {\n" +
      "    width: 25%!important\n" +
      "  }\n" +
      "}\n" +
      "</style>\n" +
      "<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n" +
      "<table bgcolor=\"#F5F5F5\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; Margin: 0 auto; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #F5F5F5; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
      "<tbody>\n" +
      "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
      "<td style=\"word-break: break-word; vertical-align: top; border-collapse: collapse;\" valign=\"top\">\n" +
      "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color:#F5F5F5\"><![endif]-->\n" +
      "<div style=\"background-color:transparent;\">\n" +
      "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;;\">\n" +
      "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
      "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n" +
      "<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:transparent;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:5px; padding-bottom:5px;\"><![endif]-->\n" +
      "<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top;;\">\n" +
      "<div style=\"width:100% !important;\">\n" +
      "<!--[if (!mso)&(!IE)]><!-->\n" +
      "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
      "<!--<![endif]-->\n" +
      "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
      "<tbody>\n" +
      "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
      "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px; border-collapse: collapse;\" valign=\"top\">\n" +
      "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" height=\"10\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 100%; border-top: 0px solid transparent; height: 10px;\" valign=\"top\" width=\"100%\">\n" +
      "<tbody>\n" +
      "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
      "<td height=\"10\" style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; border-collapse: collapse;\" valign=\"top\"><span></span></td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<!--[if (!mso)&(!IE)]><!-->\n" +
      "</div>\n" +
      "<!--<![endif]-->\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
      "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
      "</div>\n" +
      "</div>\n" +
      "</div>\n" +
      "<div style=\"background-color:transparent;\">\n" +
      "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: #D6E7F0;;\">\n" +
      "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:#D6E7F0;\">\n" +
      "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:#D6E7F0\"><![endif]-->\n" +
      "<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:#D6E7F0;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 25px; padding-left: 25px; padding-top:5px; padding-bottom:60px;\"><![endif]-->\n" +
      "<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top;;\">\n" +
      "<div style=\"width:100% !important;\">\n" +
      "<!--[if (!mso)&(!IE)]><!-->\n" +
      "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:60px; padding-right: 25px; padding-left: 25px;\">\n" +
      "<!--<![endif]-->\n" +
      "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 15px; padding-top: 20px; padding-bottom: 0px; font-family: Tahoma, Verdana, sans-serif\"><![endif]-->\n" +
      "<div style=\"color:#052d3d;font-family:'Lato', Tahoma, Verdana, Segoe, sans-serif;line-height:150%;padding-top:20px;padding-right:10px;padding-bottom:0px;padding-left:15px;\">\n" +
      "<div style=\"font-size: 12px; line-height: 18px; font-family: 'Lato', Tahoma, Verdana, Segoe, sans-serif; color: #052d3d;\">\n" +
      "<p style=\"font-size: 14px; line-height: 75px; text-align: center; margin: 0;\"><span style=\"font-size: 50px;\"><strong><span style=\"line-height: 75px; font-size: 50px;\"><span style=\"font-size: 38px; line-height: 57px;\">\n" +
      "Team service notification" +
      "</span></span></strong></span></p>\n" +
      "<p style=\"font-size: 14px; line-height: 51px; text-align: center; margin: 0;\"><span style=\"font-size: 34px;\"><strong><span style=\"line-height: 51px; font-size: 34px;\"><span style=\"color: #2190e3; line-height: 51px; font-size: 34px;\">\n" +
      "" +
      "</span></span></strong></span></p>\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if mso]></td></tr></table><![endif]-->\n" +
      "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: Tahoma, Verdana, sans-serif\"><![endif]-->\n" +
      "<div style=\"color:#555555;font-family:'Lato', Tahoma, Verdana, Segoe, sans-serif;line-height:120%;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
      "<div style=\"font-size: 12px; line-height: 14px; color: #555555; font-family: 'Lato', Tahoma, Verdana, Segoe, sans-serif;\">\n" +
      "<p style=\"font-size: 14px; line-height: 21px; text-align: center; margin: 0;\"><span style=\"font-size: 18px; color: #000000;\">\n" +
      message +
      "</span></p>\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if mso]></td></tr></table><![endif]-->\n" +
      "<div align=\"center\" class=\"button-container\" style=\"padding-top:20px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
      "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-spacing: 0; border-collapse: collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"><tr><td style=\"padding-top: 20px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px\" align=\"center\"><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"" +
      " " +
      "\" style=\"height:39pt; width:174pt; v-text-anchor:middle;\" arcsize=\"29%\" stroke=\"false\" fillcolor=\"#fc7318\"><w:anchorlock/><v:textbox inset=\"0,0,0,0\"><center style=\"color:#ffffff; font-family:Tahoma, Verdana, sans-serif; font-size:16px\"><![endif]--><a href=\"" +
      " " +
      "\" style=\"-webkit-text-size-adjust: none; text-decoration: none; display: inline-block; color: #ffffff; background-color: #fc7318; border-radius: 15px; -webkit-border-radius: 15px; -moz-border-radius: 15px; width: auto; width: auto; border-top: 1px solid #fc7318; border-right: 1px solid #fc7318; border-bottom: 1px solid #fc7318; border-left: 1px solid #fc7318; padding-top: 10px; padding-bottom: 10px; font-family: 'Lato', Tahoma, Verdana, Segoe, sans-serif; text-align: center; mso-border-alt: none; word-break: keep-all;\" target=\"_blank\"><span style=\"padding-left:40px;padding-right:40px;font-size:16px;display:inline-block;\">\n" +
      "<span style=\"font-size: 16px; line-height: 32px;\"><strong>\n" +
      "Enjoy!" +
      "</strong></span>\n" +
      "</span></a>\n" +
      "<!--[if mso]></center></v:textbox></v:roundrect></td></tr></table><![endif]-->\n" +
      "</div>\n" +
      "<!--[if (!mso)&(!IE)]><!-->\n" +
      "</div>\n" +
      "<!--<![endif]-->\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
      "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
      "</div>\n" +
      "</div>\n" +
      "</div>\n" +
      "<div style=\"background-color:transparent;\">\n" +
      "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 650px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;;\">\n" +
      "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
      "<!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:transparent;\"><tr><td align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:650px\"><tr class=\"layout-full-width\" style=\"background-color:transparent\"><![endif]-->\n" +
      "<!--[if (mso)|(IE)]><td align=\"center\" width=\"650\" style=\"background-color:transparent;width:650px; border-top: 0px solid transparent; border-left: 0px solid transparent; border-bottom: 0px solid transparent; border-right: 0px solid transparent;\" valign=\"top\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 0px; padding-left: 0px; padding-top:20px; padding-bottom:60px;\"><![endif]-->\n" +
      "<div class=\"col num12\" style=\"min-width: 320px; max-width: 650px; display: table-cell; vertical-align: top;;\">\n" +
      "<div style=\"width:100% !important;\">\n" +
      "<!--[if (!mso)&(!IE)]><!-->\n" +
      "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:20px; padding-bottom:60px; padding-right: 0px; padding-left: 0px;\">\n" +
      "<!--<![endif]-->\n" +
      "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding-right: 10px; padding-left: 10px; padding-top: 10px; padding-bottom: 10px; font-family: Tahoma, Verdana, sans-serif\"><![endif]-->\n" +
      "<div style=\"color:#555555;font-family:'Lato', Tahoma, Verdana, Segoe, sans-serif;line-height:150%;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
      "<div style=\"font-size: 12px; line-height: 18px; color: #555555; font-family: 'Lato', Tahoma, Verdana, Segoe, sans-serif;\">\n" +
      "<p style=\"font-size: 14px; line-height: 21px; text-align: center; margin: 0;\">KlapKlap Soft Team ©</p>\n" +
      "<p style=\"font-size: 14px; line-height: 21px; text-align: center; margin: 0;\">39 Corso Duca degli Abruzzi, Torino, TO 10140</p>\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if mso]></td></tr></table><![endif]-->\n" +
      "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
      "<tbody>\n" +
      "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
      "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 10px; padding-right: 10px; padding-bottom: 10px; padding-left: 10px; border-collapse: collapse;\" valign=\"top\">\n" +
      "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" height=\"0\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; width: 60%; border-top: 1px dotted #C4C4C4; height: 0px;\" valign=\"top\" width=\"60%\">\n" +
      "<tbody>\n" +
      "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
      "<td height=\"0\" style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; border-collapse: collapse;\" valign=\"top\"><span></span></td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<!--[if (!mso)&(!IE)]><!-->\n" +
      "</div>\n" +
      "<!--<![endif]-->\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
      "<!--[if (mso)|(IE)]></td></tr></table></td></tr></table><![endif]-->\n" +
      "</div>\n" +
      "</div>\n" +
      "</div>\n" +
      "<!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
      "</td>\n" +
      "</tr>\n" +
      "</tbody>\n" +
      "</table>\n" +
      "<!--[if (IE)]></div><![endif]-->\n" +
      "</body>\n" +
      "</html>"
    );
  }

  @Override
  public boolean hasErrors() {
    return error.get();
  }
}
