package com.awesome.medifofo.model;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eunsik on 2017-04-09.
 */

public class SymptomData {

    private static final String[] head = {"Agitation", "Anxiety", "Apathy", "Bald spots (hair)", "Blackouts (memory time loss)", "Bleeding", "Brittle hair", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Coma", "Compulsive behavior", "Confusion", "Craving alcohol", "Craving to eat ice, dirt or paper", "Crawling sensation", "Delusions", "Depressed mood", "Difficult to wake from sleep", "Difficulty concentrating", "Difficulty falling asleep", "Difficulty finding words", "Difficulty learning new things", "Difficulty sleeping", "Difficulty solving problems", "Difficulty staying asleep", "Difficulty staying awake during day", "Disorientation", "Dizziness", "Drainage or pus", "Drowsiness", "Early morning waking", "Easily distracted", "Emotional detachment", "Fainting", "Fatigue", "Fear of air", "Fear of gaining weight", "Fear of water", "Feeling of being detached from reality", "Feeling smothered", "Feeling something moving on scalp", "Fever", "Fits of rage", "Flashbacks", "Food cravings", "Forgetfulness", "Frightening dreams", "Frightening thoughts", "Hair loss", "Hallucinations", "Headache", "Headache (worst ever)", "Hearing voices", "Impaired color vision", "Impaired judgement", "Impaired social skills", "Impulsive behavior", "Inappropriate behavior", "Itching or burning", "Lack of emotion", "Lack of motivation", "Lack of pleasure", "Lightheadedness", "Loss of consciousness", "Lump or bulge", "Memory problems", "Mood swings", "Multiple bruises of different ages", "Numbness or tingling", "Paranoid behavior", "Personality changes", "Poor concentration", "Pulling out hair", "Punching or kicking in sleep", "Repeats phrases", "Repetitive behaviors", "Restless (tossing and turning) sleep", "Sadness", "Scratching", "Seizures (uncontrollable jerking of limbs)", "Sense of impending doom", "Skin irritation", "Sleep walking", "Slow thinking", "Spinning sensation", "Swelling", "Tenderness to touch", "Unusual behavior", "Visible bugs or parasites", "Visible deformity", "Warm to touch", "White specks on scalp or hair"};
    private static final String[] face = {"Abnormally round face", "Blank stare", "Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Drainage or pus", "Drooping of one side of face", "Enlarged or swollen glands", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Muscle twitching (painless)", "Numbness or tingling", "Pain or discomfort", "Pulling out beard", "Swelling", "Tenderness to touch", "Unusual facial expression", "Visible deformity", "Warm to touch"};
    private static final String[] eye = {"Blank stare", "Bleeding", "Bleeding in eye", "Blind spot in vision", "Blindness", "Blinking eyes", "Blurred vision", "Broken bone (single fracture)", "Brownish-Yellowish ring around the color of eye", "Bruising or discoloration", "Bulging eyes", "Change in vision", "Cloudy vision", "Decreased night vision", "Discharge or mucus in eyes", "Distortion of part of visual field", "Double vision (with one eye covered)", "Double vision (without one eye covered)", "Drainage or pus", "Drooping eyelid", "Dry eyes", "Enlarged (dilated) pupils", "Eye crusting with sleep", "Eye irritation", "Eyelashes falling out", "Eyelid redness", "Eyes do not track together", "Eyes rolling back", "Flickering lights in vision", "Flickering uncolored zig-zag line in vision", "Floating spots or strings in vision", "Frequent changes in eye glass prescription", "Frequent squinting", "Gritty or scratchy eyes", "Holding objects closer to read", "Holding objects further away to read", "Itching or burning", "Jerking eye movements", "Loss of outside 1/3 of eyebrow (unintentional)", "Loss of side vision", "Lump or bulge", "Multiple bruises of different ages", "Nasal symptoms and one red eye", "Need brighter light to read", "Numbness or tingling", "Pain or discomfort", "Pain when moving eyes", "Painful red lump on eyelid", "Partial vision loss", "Puffy eyelids", "Pulling out eyelashes", "Red (bloodshot) eyes", "Red eye (single)", "Red spots inside lower eyelid", "Scaley skin on eyelids", "See letters, numbers or musical notes as colors", "Sensation of something in eye", "Sensitive to light", "Shadow over part of vision", "Slow eye movements", "Small (constricted) pupils", "Sore or burning eyes", "Squinting eyes", "Sudden flash of lights", "Sunken eyes", "Swelling", "Tearing in one eye", "Tenderness to touch", "Tilts head to look at something", "Trouble distinguishing color shades", "Unable to blink or close eyelid", "Unequal pupils (size)", "Visible deformity", "Vision fading of colors", "Visual halos around lights", "Warm to touch", "Watery eyes", "Yellow eyes"};
    private static final String[] nose = {"Bruising or discoloration", "Decreased smell", "Difficulty breathing", "Difficulty breathing through nose", "Drainage or pus", "Episodes of not breathing during sleep", "Frequent infections", "Itching or burning", "Lump or bulge", "Multiple bruises of different ages", "Nasal congestion", "Nasal symptoms and one red eye", "Noisy breathing", "Nosebleed", "Numbness or tingling", "Pain or discomfort", "Runny nose", "Sneezing", "Snoring", "Strange smell or taste", "Swelling", "Tenderness to touch", "Visible deformity", "Warm to touch"};
    private static final String[] ear = {"Bleeding", "Bruising or discoloration", "Drainage or pus", "Ear ache", "Hearing loss", "Itching or burning", "Lump or bulge", "Multiple bruises of different ages", "Numbness or tingling", "Pain or discomfort", "Ringing in ears", "Sensitive to noise", "Swelling", "Taste words when they are heard", "Tenderness to touch", "Warm to touch"};
    private static final String[] mouth = {"Bad breath", "Bad taste in mouth", "Belching", "Bitter almond odor on breath", "Bleeding", "Bleeding gums", "Bruising or discoloration", "Choking", "Coated or furry tongue", "Colored sputum", "Cough", "Cracks at corner of mouth", "Damaged teeth enamel", "Decreased appetite", "Decreased taste", "Difficulty opening mouth", "Difficulty swallowing", "Difficulty talking", "Drainage or pus", "Drinking excessive fluids", "Drooling", "Dry mouth", "Episodes of not breathing during sleep", "Excessive mouth watering", "Frequent chewing", "Fruity odor on breath", "Gagging", "Grinding teeth", "Grooved tongue", "Gum sores", "Hoarse voice", "Increased speech volume", "Increased talkativeness", "Increased thirst", "Involuntary movements (picking, lip smacking etc.)", "Itching or burning", "Jaw locking", "Loss of voice", "Lump or bulge", "Metallic taste in mouth", "Mouth sores", "Muffled voice", "Multiple bruises of different ages", "Noisy breathing", "Numbness or tingling", "Pain or discomfort", "Rapid speech", "Red (strawberry) tongue", "Red gums", "Red spots", "Regurgitation of food or liquid", "Shortness of breath", "Slurred speech", "Smooth tongue", "Snoring", "Sore tongue", "Soreness or burning inside of mouth", "Spots on throat", "Spots on tonsils", "Strange smell or taste", "Swelling", "Swollen gums", "Swollen lips", "Swollen tongue", "Swollen tonsils", "Taste of acid in mouth", "Taste words when they are heard", "Teeth do not fit like they used to", "Tenderness to touch", "Thick saliva or mucus", "Unable to open mouth (jaw)", "Uncontrollable verbal outbursts", "Unusual taste in mouth", "Upset stomach", "Warm to touch", "White patches inside mouth", "White patches on tongue"};
    private static final String[] jaw = {"Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Clicking or popping sound from jaw", "Difficulty moving joint", "Difficulty opening mouth", "Drainage or pus", "Enlarged or swollen glands", "Jaw locking", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Pulling out beard", "Stiffness or decreased movement", "Swelling", "Tender glands", "Tenderness to touch", "Unable to open mouth (jaw)", "Visible deformity", "Warm to touch"};
    private static final String[] neck = {"Bleeding", "Bruising or discoloration", "Choking", "Choking on food", "Cough", "Difficulty swallowing", "Difficulty talking", "Drainage or pus", "Enlarged or swollen glands", "Food getting stuck (swallowing)", "Gagging", "Hoarse voice", "Involuntary head turning or twisting", "Itching or burning", "Loss of voice", "Lump or bulge", "Muffled voice", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Muscle twitching (painless)", "Noisy breathing", "Numbness or tingling", "Pain or discomfort", "Post nasal drip", "Short, wide neck", "Sore throat", "Spots on throat", "Spots on tonsils", "Stiff neck", "Swelling", "Swollen tonsils", "Tender glands", "Tenderness to touch", "Throat tightness", "Tilts head to look at something", "Visible deformity", "Visible pulsations", "Warm to touch"};
    private static final String[] chest = {"Bleeding", "Bleeding from nipple", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Cough", "Difficulty breathing", "Difficulty talking", "Discharge from nipple", "Drainage or pus", "Episodes of not breathing during sleep", "Feeling of not being able to get enough air", "Food getting stuck (swallowing)", "Frequent infections", "Heartburn", "Hyperventilating (rapid/deep breathing)", "Irregular heartbeat", "Labored breathing", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "New onset asthma", "Nighttime wheezing", "Noisy breathing", "Numbness or tingling", "Pain or discomfort", "Palpitations (fluttering in chest)", "Pounding heart (pulse)", "Pressure or heaviness", "Prolonged breathing pauses", "Rapid breathing", "Rapid heart rate (pulse)", "Shortness of breath", "Slow heart rate (pulse)", "Slow or irregular breathing", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Tightness", "Visible deformity", "Warm to touch", "Wheezing"};
    private static final String[] belly = {"Bleeding", "Bloating or fullness", "Bloody or red colored vomit", "Bruising or discoloration", "Bulging veins", "Change in bowel habits", "Coffee grounds colored vomit", "Constipation", "Diarrhea", "Distended stomach", "Drainage or pus", "Foul smelling stools", "Frequent bowel movements", "Frequent urge to have bowel movement", "Increased passing gas", "Intentional vomiting (purging)", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Nausea or vomiting", "Numbness or tingling", "Pain or discomfort", "Pain with sexual intercourse", "Painful bowel movements", "Pressure or fullness", "Pulsating sensation", "Stomach cramps", "Straining with bowel movements", "Swelling", "Tenderness to touch", "Upset stomach", "Visible deformity", "Warm to touch", "Worms in stool"};
    private static final String[] back = {"Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Curved spine", "Difficulty walking", "Drainage or pus", "Hunched or stooped posture", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Visible deformity", "Warm to touch"};
    private static final String[] spine = {"Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Curved spine", "Difficulty walking", "Drainage or pus", "Hunched or stooped posture", "Joint pain", "Lump or bulge", "Morning joint stiffness", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Visible deformity", "Warm to touch"};
    private static final String[] arms = {"Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Bulging veins", "Difficulty moving joint", "Drainage or pus", "Inability to move", "Joint aches", "Joint pain", "Lump or bulge", "Morning joint stiffness", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Unable to move arm", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] elbow = {"Bleeding", "Broken bone (single fracture)", "Bruising or discoloration", "Difficulty moving joint", "Drainage or pus", "Enlarged or swollen glands", "Guarding or favoring joint", "Inability to move", "Joint aches", "Joint instability", "Joint pain", "Lump or bulge", "Morning joint stiffness", "Multiple bruises of different ages", "Numbness or tingling", "Stiffness or decreased movement", "Swelling", "Tender glands", "Tenderness to touch", "Unable to move joint", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] hands = {"Bleeding", "Blue colored skin", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Cold hands", "Color change", "Drainage or pus", "Excessive sweating", "Involuntary movements (picking, lip smacking etc.)", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Muscle twitching (painless)", "Numbness or tingling", "Pain or discomfort", "Shaking hands or tremor", "Single palm crease", "Swelling", "Tenderness to touch", "Unable to grip (hands)", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] finger = {"Black colored skin", "Bleeding", "Blue colored skin", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Cold hands", "Color change", "Curved fingernails", "Difficulty moving joint", "Drainage or pus", "Enlarged finger tips", "Inability to move", "Involuntary movements (picking, lip smacking etc.)", "Joint pain", "Lump or bulge", "Morning joint stiffness", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Muscle twitching (painless)", "Numbness or tingling", "Pain or discomfort", "Red or black spots on fingernails", "Shaking hands or tremor", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Unable to grip (hands)", "Unusually short fourth fingers", "Upward curving (spooning) of nails", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] legs = {"Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Bulging veins", "Drainage or pus", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Unable to bear weight", "Unable to move leg", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] hip = {"Bleeding", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Black (tar) colored stools", "Blood in toilet", "Blood on stool surface", "Blood on toilet tissue", "Bloody or red colored stools", "Bulging veins", "Change in stools", "Difficulty climbing stairs", "Difficulty getting up from a chair", "Difficulty moving joint", "Difficulty walking", "Drainage or pus", "Joint aches", "Joint instability", "Joint locking or catching", "Joint pain", "Lump or bulge", "Morning joint stiffness", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Painful bowel movements", "Protruding rectal material", "Stool leaking (incontinence)", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Unable to bear weight", "Unable to move joint", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] ankle = {"Bleeding", "Broken bone (single fracture)", "Bruising or discoloration", "Difficulty moving joint", "Difficulty walking", "Drainage or pus", "Guarding or favoring joint", "Joint aches", "Joint pain", "Lump or bulge", "Morning joint stiffness", "Multiple bruises of different ages", "Numbness or tingling", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Unable to bear weight", "Unable to bend foot down", "Unable to move joint", "Visible deformity", "Warm to touch", "Weakness",};
    private static final String[] foot = {"Bleeding", "Blue colored skin", "Broken bone (single fracture)", "Broken bones (multiple fractures)", "Bruising or discoloration", "Cold feet", "Color change", "Difficulty walking", "Drainage or pus", "Lump or bulge", "Multiple bruises of different ages", "Muscle cramps or spasms (painful)", "Numbness or tingling", "Pain or discomfort", "Shuffling gait (feet)", "Stiffness or decreased movement", "Swelling", "Tenderness to touch", "Unable to bear weight", "Visible deformity", "Warm to touch", "Weakness"};
    private static final String[] man = {"Bleeding", "Blood or red colored urine", "Bruising or discoloration", "Cloudy urine with strong odor", "Curved or bent penis during erection", "Dark colored (brown) urine", "Decreased urination", "Difficulty starting urine stream", "Difficulty urinating", "Discharge from penis", "Drainage or pus", "Erectile dysfunction", "Frequent nighttime urination", "Frequent urge to urinate", "Frequent urination", "Itching or burning", "Lump or bulge", "Multiple bruises of different ages", "Numbness or tingling", "Pain during erection", "Pain or discomfort", "Pain with sexual intercourse (male)", "Pain with urination", "Painful ejaculation", "Premature ejaculation", "Pressure or fullness", "Slow or weak urine stream", "Sudden urge to urinate", "Swelling", "Tenderness to touch", "Testicles shrinkage", "Testicular pain", "Unable to obtain or maintain erection", "Urine leaking (incontinence)", "Vaginal odor", "Visible bugs or parasites", "Warm to touch"};
    private static final String[] woman = {"Bleeding", "Blood or red colored urine", "Bruising or discoloration", "Cloudy urine with strong odor", "Dark colored (brown) urine", "Decreased urination", "Difficulty starting urine stream", "Difficulty urinating", "Drainage or pus", "Frequent urge to urinate", "Frequent urination", "Heavy menstrual bleeding", "Irregular menstrual periods", "Itching or burning", "Lump or bulge", "Missed or late menstrual period", "Multiple bruises of different ages", "No menstrual periods", "Numbness or tingling", "Pain or discomfort", "Pain with sexual intercourse (female)", "Pain with urination", "Pressure or fullness", "Protruding vaginal material", "Slow or weak urine stream", "Sudden urge to urinate", "Swelling", "Tenderness to touch", "Urine leaking (incontinence)", "Vaginal bleeding", "Vaginal bleeding after menopause", "Vaginal bleeding between periods", "Vaginal discharge", "Vaginal dryness", "Vaginal odor", "Visible bugs or parasites", "Warm to touch"};
    private static final String[] digestive = {};
    private static final String[] respiratory = {};
    private static final String[] heart = {};

    public static String symptomList[][] = {
            head, face, eye, nose, ear, mouth, jaw, neck, chest, belly, back, spine, arms, elbow, hands, finger, legs, hip, ankle,
            foot, man, woman, digestive, respiratory, heart
    };

    public static List<ListItem> getListData() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < head.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(head[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData2() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < face.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(face[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData3() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < eye.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(eye[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData4() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < nose.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(nose[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData5() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < ear.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(ear[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData6() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < mouth.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(mouth[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData7() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < jaw.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(mouth[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData8() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < neck.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(neck[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData9() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < chest.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(chest[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData10() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < belly.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(belly[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData11() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < back.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(back[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData12() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < spine.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(spine[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData13() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < arms.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(arms[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData14() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < elbow.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(elbow[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData15() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < hands.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(hands[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData16() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < finger.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(finger[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData17() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < legs.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(legs[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData18() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < hip.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(hip[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData19() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < ankle.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(ankle[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData20() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < foot.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(foot[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData21() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < man.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(man[i]);
            data.add(item);
        }

        return data;
    }

    public static List<ListItem> getListData22() {
        List<ListItem> data = new ArrayList<>();

        for (int i = 0; i < woman.length; i++) {
            ListItem item = new ListItem();
            item.setTitle(woman[i]);
            data.add(item);
        }

        return data;
    }
}

