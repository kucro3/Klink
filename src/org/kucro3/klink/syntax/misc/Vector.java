package org.kucro3.klink.syntax.misc;

import org.kucro3.klink.syntax.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Vector implements Cloneable {
    public Vector(String prefix, String suffix, String separator)
    {
        this(prefix, suffix, separator, Integer.MAX_VALUE);
    }

    public Vector(String prefix, String suffix, String separator, int limit)
    {
        this.prefix = Objects.requireNonNull(prefix, "prefix");
        this.suffix = Objects.requireNonNull(suffix, "suffix");
        this.separator = Objects.requireNonNull(separator, "separator");
        this.limit = limit;

        if(limit < 0)
            throw new IllegalArgumentException("limit");
    }

    public Vector parse(Sequence seq)
    {
        reset();

        String content = record(seq.next()).trim();

        if(!content.startsWith(prefix))
        {
            excepted = true;
            return this;
        }

        if(content.endsWith(suffix))
        {
            content = content.substring(prefix.length(), content.length() - suffix.length());

            if(content.isEmpty())
                this.parsed = new String[0];
            else
                this.parsed = new String[] {content};

            return this;
        }

        StringBuilder buffer = new StringBuilder(content.substring(prefix.length()));
        while(true)
        {
            content = record(seq.next()).trim();
            if(content.endsWith(suffix))
            {
                buffer.append(" ").append(content.substring(0, content.length() - suffix.length()));
                break;
            }
            else
                buffer.append(" ").append(content);
        }

        String[] splitted = buffer.toString().split(separator);

        if(splitted.length > limit)
        {
            this.excepted = true;
            this.outOfLimit = true;
            return this;
        }

        this.parsed = new String[splitted.length];

        for(int i = 0; i < splitted.length; i++)
            this.parsed[i] = splitted[i].trim();

        return this;
    }

    public String getPrefix()
    {
        return prefix;
    }

    public Vector setPrefix(String prefix)
    {
        return new Vector(prefix, suffix, separator);
    }

    public String getSuffix()
    {
        return suffix;
    }

    public Vector setSuffix(String suffix)
    {
        return new Vector(prefix, suffix, separator);
    }

    public String getSeparator()
    {
        return separator;
    }

    public Vector setSeparator(String separator)
    {
        return new Vector(prefix, suffix, separator);
    }

    public Vector set(String prefix, String suffix)
    {
        return set(prefix, suffix, separator);
    }

    public Vector set(String prefix, String suffix, String separator)
    {
        return new Vector(prefix, suffix, separator);
    }

    public boolean outOfLimit()
    {
        return outOfLimit;
    }

    public boolean isExcepted()
    {
        return excepted;
    }

    public String[] getLastParsed()
    {
        return parsed;
    }

    public boolean hasLastParsed()
    {
        return parsed != null;
    }

    public List<String> getLastReaded()
    {
        return Collections.unmodifiableList(lastReaded);
    }

    String record(String readed)
    {
        lastReaded.add(readed);
        return readed;
    }

    public void reset()
    {
        excepted = false;
        outOfLimit = false;
        lastReaded.clear();
        parsed = null;
    }

    @Override
    public Vector clone()
    {
        return new Vector(prefix, suffix, separator);
    }

    private final int limit;

    private boolean outOfLimit;

    private boolean excepted;

    private final List<String> lastReaded = new ArrayList<>();

    private String[] parsed;

    private final String prefix;

    private final String suffix;

    private final String separator;
}
