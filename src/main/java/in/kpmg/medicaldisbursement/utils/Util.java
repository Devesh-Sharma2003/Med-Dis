package in.kpmg.medicaldisbursement.utils;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class Util {

    public String getClientIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }

        if (ipAddress.equals("0:0:0:0:0:0:0:1") || ipAddress.equals("::1")) {
            ipAddress = "127.0.0.1";  // Convert to IPv4 loopback
        }

        return ipAddress;
    }
}
