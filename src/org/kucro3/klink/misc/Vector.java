package org.kucro3.klink.misc;

import org.kucro3.klink.syntax.Sequence;

import java.util.Objects;

public class Vector implements Cloneable {
    public Vector(String prefix, String suffix, String separator)
    {
        this(prefix, suffix, separator, Integer.MAX_VALUE);
    }

    public Vector(String prefix, String suffix, String separator, int limit)
    {
        this.prefix = Objects.requireNonNull(prefix);
        this.suffix = Objects.requireNonNull(suffix);
        this.separator = Objects.requireNonNull(separator);
        this.limit = limit;
    }

    public Vector parse(Sequence seq)
    {
        excepted = false;
        outOfLimit = false;

        String content = seq.next().trim();

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
            content = seq.next().trim();
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

    public void reset()
    {
        excepted = false;
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

    private String[] parsed;

    private final String prefix;

    private final String suffix;

    private final String separator;
}
