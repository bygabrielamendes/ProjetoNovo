import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.io.File;
/* para adicionar essas bibliotecas segue o passo a passo:
Pressione Ctrl + Shift + P.

Digite "Java: Configure Java Runtime".
em Libraries adicione os 5 arquivos da pasta
e salve. */
public class EmailService {

    public static void enviarComAnexo(String para, String assunto, String mensagem, String caminhoAnexo) {
        // Dados da conta (Remetente)
        final String usuario = "noreply.helpdesksantos@gmail.com";
        final String senha = "ungirihzcmyypjgb"; 

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // Garante compatibilidade com Java moderno

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(usuario, senha);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(para));
            message.setSubject(assunto);

            // Parte 1: Texto
            MimeBodyPart corpoEmail = new MimeBodyPart();
            corpoEmail.setText(mensagem);

            // Parte 2: Anexo
            MimeBodyPart anexoPart = new MimeBodyPart();
            File file = new File(caminhoAnexo);
            
            // Verificação de segurança: o arquivo existe?
            if (!file.exists()) {
                System.err.println("Erro: O arquivo não foi encontrado em: " + caminhoAnexo);
                return;
            }
            
            anexoPart.attachFile(file);

            // Juntando as partes
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(corpoEmail);
            multipart.addBodyPart(anexoPart);

            message.setContent(multipart);

            System.out.println("Iniciando envio para " + para + "...");
            Transport.send(message);
            System.out.println("E-mail enviado com sucesso!");

        } catch (Exception e) {
            e.printStackTrace(); // Mostra o erro detalhado no console do VS Code
        }
    }

    public static void main(String[] args) {
        // Agora os dados ficam aqui no main, deixando o método enviarComAnexo reutilizável
        String destinatario = "gabriela@hdesk.com.br";
        String titulo = "Arquivos de teste";
        String texto = "Segue os arquivos do cara x";
        String arquivo = "C:\\Users\\Suporte\\Desktop\\NFCe_XML_Dezembro_2025.zip";

        enviarComAnexo(destinatario, titulo, texto, arquivo);
    }
}