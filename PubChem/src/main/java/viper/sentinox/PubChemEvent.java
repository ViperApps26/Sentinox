package viper.sentinox;

public class PubChemEvent {

    private final long ts;
    private final String ss;
    private final String medicine;
    private final String cid;
    private final String reaction;

    public PubChemEvent(long ts, String ss, String medicine, String cid, String reaction) {
        this.ts = ts;
        this.ss = ss;
        this.medicine = medicine;
        this.cid = cid;
        this.reaction = reaction;
    }

    public long getTs() {
        return ts;
    }

    public String getSs() {
        return ss;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getCid() {
        return cid;
    }

    public String getReaction() {
        return reaction;
    }
}