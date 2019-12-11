package com.ccz.apps.maple.misssaigon;

import com.ccz.apps.maple.misssaigon.common.config.MissSaigonConfig;
import com.ccz.apps.maple.misssaigon.controller.admin.AdminCommandAction;
import com.ccz.apps.maple.misssaigon.controller.board.BoardCommandAction;
import com.ccz.apps.maple.misssaigon.controller.channel.ChannelCommandAction;
import com.ccz.apps.maple.misssaigon.controller.file.FileCommandAction;
import com.ccz.apps.maple.misssaigon.controller.friend.FriendCommandAction;
import com.ccz.apps.maple.misssaigon.controller.message.MessageCommandAction;
import com.ccz.apps.maple.misssaigon.controller.oauth.OAuthCommandAction;
import com.ccz.apps.maple.misssaigon.controller.user.UserCommandAction;
import com.ccz.modules.common.dbhelper.DbConnMgr;
import com.ccz.modules.common.utils.ResourceList;
import com.ccz.modules.domain.inf.IBizMain;
import com.ccz.modules.domain.inf.ICommandFunction;
import com.ccz.modules.server.ServiceServer;
import com.ccz.modules.server.service.DbAdminManager;
import com.ccz.modules.server.service.DbAppManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

@Slf4j
@Service
public class MissSaigonMain implements IBizMain {

    @Autowired
    MissSaigonConfig missSaigonConfig;

    @Autowired
    AdminCommandAction adminCommandAction;

    @Autowired
    BoardCommandAction boardCommandAction;

    @Autowired
    ChannelCommandAction channelCommandAction;

    @Autowired
    FileCommandAction fileCommandAction;

    @Autowired
    FriendCommandAction friendCommandAction;

    @Autowired
    MessageCommandAction messageCommandAction;

    @Autowired
    OAuthCommandAction oAuthCommandAction;

    @Autowired
    UserCommandAction userCommandAction;

    @Override
    public void init() {
        try {
            DbAdminManager.getInst().initAdminCommon(missSaigonConfig.getPoolName(), missSaigonConfig.getMySqlHost(), missSaigonConfig.getMySqlOptions(), missSaigonConfig.getMySqlUsername(), missSaigonConfig.getMySqlPassword());
            if(DbAdminManager.getInst().createAdminDatabase() == true) {
                DbAdminManager.getInst().removeAdminConnectionPoolForCreateDatabase();
                DbAdminManager.getInst().createAdminConnectionPool();
                DbAdminManager.getInst().createAdminTables();
            }
            DbAppManager.getInst().initInternalDb(missSaigonConfig.getPoolName(), missSaigonConfig.getMySqlHost(), missSaigonConfig.getMySqlOptions(), missSaigonConfig.getMySqlUsername(), missSaigonConfig.getMySqlPassword());
        }catch(Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public Map<String, ICommandFunction> getCommandFunctions() {
        Map<String, ICommandFunction> cmdMap = new ConcurrentHashMap<>();
        cmdMap.putAll(adminCommandAction.getCommandFunctions());
        cmdMap.putAll(boardCommandAction.getCommandFunctions());
        cmdMap.putAll(channelCommandAction.getCommandFunctions());
        cmdMap.putAll(fileCommandAction.getCommandFunctions());
        cmdMap.putAll(friendCommandAction.getCommandFunctions());
        cmdMap.putAll(messageCommandAction.getCommandFunctions());
        cmdMap.putAll(oAuthCommandAction.getCommandFunctions());
        cmdMap.putAll(userCommandAction.getCommandFunctions());
        return cmdMap;
    }

    private URLClassLoader createURLClassLoader() {
        Collection<String> resources = ResourceList.getResources(Pattern.compile(".*\\.sql"));
        Collection<URL> urls = new ArrayList<URL>();
        for (String resource : resources) {
            File file = new File(resource);
            // Ensure that the JAR exists
            // and is in the globalclasspath directory.
            if (file.isFile() && "globalclasspath".equals(file.getParentFile().getName())) {
                try {
                    log.info(file.toURI().toURL().toString());
                    urls.add(file.toURI().toURL());
                } catch (MalformedURLException e) {
                    // This should never happen.
                    e.printStackTrace();
                }
            }
        }
        return new URLClassLoader(urls.toArray(new URL[urls.size()]));
    }
}
