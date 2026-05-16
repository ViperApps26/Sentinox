import org.junit.jupiter.api.Test;
import viper.sentinox.model.JointAnalysis;

import static org.junit.jupiter.api.Assertions.*;

class JointAnalysisTest {

    @Test
    void constructor_storesValuesCorrectly() {
        JointAnalysis analysis = new JointAnalysis(
                2,
                4,
                50.0,
                "Moderate agreement"
        );

        assertEquals(2, analysis.getMatchedReactions());
        assertEquals(4, analysis.getTotalReactions());
        assertEquals(50.0, analysis.getAgreementPercentage());
        assertEquals("Moderate agreement", analysis.getConclusion());
    }
}
