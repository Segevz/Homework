import com.sun.org.apache.xml.internal.security.utils.Constants;
import weka.classifiers.functions.SMO;
import weka.classifiers.functions.supportVector.Kernel;
import weka.core.Instance;
import weka.core.Instances;

public class SVM {
    public SMO m_smo;

    public SVM() {
        this.m_smo = new SMO();
    }

    public void buildClassifier(Instances instances) throws Exception {
        m_smo.buildClassifier(instances);
    }

    public void setKernel(Kernel kernel) {
        m_smo.setKernel(kernel);
    }

    public void setC(double c) {
        m_smo.setC(c);
    }

    public double getC() {
        return m_smo.getC();
    }

    public int[] calcConfusion(Instances instances) throws Exception {
        int truePositive = 0, falsePositive = 0, trueNegative = 0, falseNegative = 0;
        double classified, classValue;

        for (Instance instance : instances) {

            classified = m_smo.classifyInstance(instance);
            classValue = instance.classValue();

            if (classified == 1.0 && classValue == 1.0) {
                truePositive++;
            } else if (classified == 1.0 && classValue == 0.0) {
                falsePositive++;
            } else if (classified == 0.0 && classValue == 1.0) {
                falseNegative++;
            } else {
                trueNegative++;
            }
        }
        return new int[]{truePositive, falsePositive, trueNegative, falseNegative};
    }


    public double[] calculateKernelValue(Instances data) throws Exception {
        int[] values = calcConfusion(data);
        double TPR = 1.0 * values[0] / (values[0] + values[3]);
        double FPR = 1.0 * values[1] / (values[1] + values[2]);

        return new double[]{TPR, FPR};
    }
}