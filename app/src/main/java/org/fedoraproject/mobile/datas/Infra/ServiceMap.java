package org.fedoraproject.mobile.datas.Infra;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien De Nadai on 02/11/2014.
 */
public class ServiceMap {
    @Expose
    private Service ask;
    @Expose
    private Service badges;
    @Expose
    private Service blockerbugs;
    @Expose
    private Service bodhi;
    @Expose
    private Service copr;
    @Expose
    private Service darkserver;
    @Expose
    private Service docs;
    @Expose
    private Service elections;
    @Expose
    private Service fas;
    @Expose
    private Service fedmsg;
    @Expose
    private Service fedocal;
    @Expose
    private Service fedorahosted;
    @Expose
    private Service fedorapaste;
    @Expose
    private Service freemedia;
    @Expose
    private Service koji;
    @Expose
    private Service mailinglists;
    @Expose
    private Service mirrorlist;
    @Expose
    private Service mirrormanager;
    @Expose
    private Service packages;
    @Expose
    private Service people;
    @Expose
    private Service pkgdb;
    @Expose
    private Service pkgs;
    @Expose
    private Service tagger;
    @Expose
    private Service website;
    @Expose
    private Service wiki;
    @Expose
    private Service zodbot;

    public List<Service> getServiceList() {
        ArrayList<Service> services = new ArrayList<Service>();
        services.add(ask);
        services.add(badges);
        services.add(blockerbugs);
        services.add(bodhi);
        services.add(copr);
        services.add(darkserver);
        services.add(docs);
        services.add(elections);
        services.add(fas);
        services.add(fedmsg);
        services.add(fedocal);
        services.add(fedorahosted);
        services.add(fedorapaste);
        services.add(freemedia);
        services.add(koji);
        services.add(mailinglists);
        services.add(mirrorlist);
        services.add(mirrormanager);
        services.add(packages);
        services.add(people);
        services.add(pkgdb);
        services.add(pkgs);
        services.add(tagger);
        services.add(website);
        services.add(wiki);
        services.add(zodbot);
        return services;
    }
}
